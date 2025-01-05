import asyncio
import json
import websockets
from websockets.server import serve
from websockets.client import connect

class AuthenticationServer:
    def __init__(self):
        self.db_uri = "ws://localhost:8765"
        self.clients = set()
        self.db_websocket = None
    
    async def connect_to_db(self):
        while True:
            try:
                self.db_websocket = await connect(self.db_uri)
                print("Connected to database server")
                async for message in self.db_websocket:
                    print(f"Received from DB: {message}")
                    await self.handle_db_response(message)
            except Exception as e:
                print(f"Database connection error: {e}")
                self.db_websocket = None
                await asyncio.sleep(5)  # Wait before retry
    
    async def handle_client(self, websocket):
        try:
            self.clients.add(websocket)
            print(f"Client connected. Total clients: {len(self.clients)}")
            
            async for message in websocket:
                print(f"Received from client: {message}")
                if self.db_websocket:
                    await self.db_websocket.send(message)
                else:
                    await websocket.send(json.dumps({
                        "type": "error",
                        "message": "Database connection not available"
                    }))
        
        except websockets.exceptions.ConnectionClosed:
            print("Client connection closed")
        finally:
            self.clients.remove(websocket)
            print(f"Client disconnected. Total clients: {len(self.clients)}")
    
    async def handle_db_response(self, message):
        if self.clients:
            websockets_to_remove = set()
            
            for client in self.clients:
                try:
                    await client.send(message)
                except websockets.exceptions.ConnectionClosed:
                    websockets_to_remove.add(client)
                except Exception as e:
                    print(f"Error sending to client: {e}")
                    websockets_to_remove.add(client)
            
            # Remove closed connections
            self.clients -= websockets_to_remove
    
    async def start(self):
        # Start database connection
        db_connection_task = asyncio.create_task(self.connect_to_db())
        
        async with serve(self.handle_client, "localhost", 8766):
            print("Authentication server running on ws://localhost:8766")
            try:
                await asyncio.Future()  # run forever
            except Exception as e:
                print(f"Server error: {e}")
            finally:
                if self.db_websocket:
                    await self.db_websocket.close()
                db_connection_task.cancel()

if __name__ == "__main__":
    auth_server = AuthenticationServer()
    asyncio.run(auth_server.start())