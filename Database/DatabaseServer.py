import asyncio
import json
import psycopg2
from psycopg2 import sql
import websockets
from threading import Lock
import os

from dotenv import load_dotenv

load_dotenv()

class DatabaseConnection:
    _instance = None
    _lock = Lock()
    _connection = None
    
    def __new__(cls):
        with cls._lock:
            if cls._instance is None:
                cls._instance = super(DatabaseConnection, cls).__new__(cls)
        return cls._instance
    
    def __init__(self):
        if not hasattr(self, 'initialized'):
            self.initialized = True
            self.db_config = {
                'host': os.getenv('host'),
                'database': os.getenv('database'),
                'user': os.getenv('user'),
                'password': os.getenv('password')
            }
    
    def get_connection(self):
        if self._connection is None or self._connection.closed:
            self._connection = psycopg2.connect(**self.db_config)
        return self._connection
    
    def close_connection(self):
        if self._connection and not self._connection.closed:
            self._connection.close()
            self._connection = None

def handle_login(username, password):
    try:
        db = DatabaseConnection()
        conn = db.get_connection()
        cur = conn.cursor()
        
        # Add debugging print statements
        print(f"Attempting login for username: {username}")
        
        query = sql.SQL("SELECT password FROM authentication WHERE username = %s AND blocked = FALSE")
        cur.execute(query, (username,))
        result = cur.fetchone()
        
        print(f"Query result: {result}")
        
        cur.close()
        
        if result and result[0] == password:
            return "match"
        return "unmatched"
    except Exception as e:
        print(f"Login error: {e}")
        return str(e)

def handle_signup(username, display_name, password):
    try:
        db = DatabaseConnection()
        conn = db.get_connection()
        cur = conn.cursor()
        
        # Add debugging print statements
        print(f"Attempting signup for username: {username}")
        
        query = sql.SQL("INSERT INTO authentication (username, display_name, password) VALUES (%s, %s, %s)")
        cur.execute(query, (username, display_name, password))
        conn.commit()
        cur.close()
        
        print("Signup successful")
        return "success"
    except psycopg2.IntegrityError:
        print("Username already exists")
        return "username_already_exists"
    except Exception as e:
        print(f"Signup error: {e}")
        return str(e)

async def websocket_handler(websocket, path="any"):
    try:
        print("New websocket connection established")
        async for message in websocket:
            try:
                print(f"Received message: {message}")
                data = json.loads(message)
                request_type = data.get("type")
                
                if request_type == "login":
                    username = data.get("username")
                    password = data.get("password")
                    response = handle_login(username, password)
                    await websocket.send(json.dumps({
                        "type": "login_response",
                        "status": response
                    }))
                
                elif request_type == "signup":
                    username = data.get("username")
                    display_name = data.get("display_name")
                    password = data.get("password")
                    response = handle_signup(username, display_name, password)
                    await websocket.send(json.dumps({
                        "type": "signup_response",
                        "status": response
                    }))
                
                else:
                    await websocket.send(json.dumps({
                        "type": "error",
                        "message": "Invalid request type"
                    }))
            
            except json.JSONDecodeError:
                await websocket.send(json.dumps({
                    "type": "error",
                    "message": "Invalid JSON format"
                }))
            except Exception as e:
                print(f"Error handling message: {e}")
                await websocket.send(json.dumps({
                    "type": "error",
                    "message": f"Server error: {str(e)}"
                }))
    
    except websockets.exceptions.ConnectionClosed:
        print("WebSocket connection closed")
    except Exception as e:
        print(f"Unhandled error in websocket handler: {e}")

async def main():
    try:
        server = await websockets.serve(websocket_handler, "localhost", 8765)
        print("WebSocket server running on ws://localhost:8765")
        await server.wait_closed()
    finally:
        DatabaseConnection().close_connection()

if __name__ == "__main__":
    asyncio.run(main())
