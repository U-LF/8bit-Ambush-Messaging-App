import asyncio
import json
import websockets
import sys
from asyncio import Queue

class AuthenticationClient:
    def __init__(self):
        self.server_uri = "ws://localhost:8766"
        self.websocket = None
        self.running = True
        self.message_queue = Queue()
        self.response_received = asyncio.Event()

    async def connect(self):
        try:
            self.websocket = await websockets.connect(self.server_uri)
            print("Connected to authentication server")
            return True
        except Exception as e:
            print(f"Connection error: {e}")
            return False

    async def receive_messages(self):
        try:
            async for message in self.websocket:
                try:
                    # Parse and display server response
                    response = json.loads(message)
                    
                    if response.get("type") == "login_response":
                        status = response.get("status")
                        if status == "match":
                            print("\n✅ Login successful! Welcome back!")
                        elif status == "unmatched":
                            print("\n❌ Login failed: Incorrect username or password")
                        else:
                            print(f"\n❌ Login failed: {status}")

                    elif response.get("type") == "signup_response":
                        status = response.get("status")
                        if status == "success":
                            print("\n✅ Sign up successful! You can now login")
                        elif status == "username_already_exists":
                            print("\n❌ Sign up failed: Username already exists")
                        else:
                            print(f"\n❌ Sign up failed: {status}")

                    elif response.get("type") == "error":
                        print(f"\n❌ Error: {response.get('message')}")
                        
                    else:
                        print(f"\nUnknown response: {message}")
                    
                    # Signal that we received a response
                    self.response_received.set()
                    print("\nPress Enter to continue...")

                except json.JSONDecodeError:
                    print(f"\n❌ Error: Invalid response format from server")
                    self.response_received.set()

        except websockets.exceptions.ConnectionClosed:
            print("\nConnection to server closed")
            self.running = False
        except Exception as e:
            print(f"\nError receiving message: {e}")
            self.running = False
        finally:
            self.response_received.set()  # Ensure we don't deadlock

    async def send_request(self, request):
        try:
            # Reset the response event
            self.response_received.clear()
            
            # Send the request
            await self.websocket.send(json.dumps(request))
            
            # Wait for response with timeout
            try:
                await asyncio.wait_for(self.response_received.wait(), timeout=5.0)
            except asyncio.TimeoutError:
                print("\n❌ Server response timeout. Please try again.")
                
        except websockets.exceptions.ConnectionClosed:
            print("\n❌ Connection lost to server")
            self.running = False
        except Exception as e:
            print(f"\n❌ Error sending request: {e}")
            self.running = False

    async def login(self):
        try:
            print("\n=== Login ===")
            username = input("Enter username: ").strip()
            if not username:
                print("\n❌ Username cannot be empty")
                return
                
            password = input("Enter password: ").strip()
            if not password:
                print("\n❌ Password cannot be empty")
                return
            
            request = {
                "type": "login",
                "username": username,
                "password": password
            }
            
            print("\nProcessing login request...")
            await self.send_request(request)

        except Exception as e:
            print(f"\nError during login: {e}")

    async def signup(self):
        try:
            print("\n=== Sign Up ===")
            username = input("Enter username: ").strip()
            if not username:
                print("\n❌ Username cannot be empty")
                return
                
            display_name = input("Enter display name: ").strip()
            if not display_name:
                print("\n❌ Display name cannot be empty")
                return
                
            password = input("Enter password: ").strip()
            if not password:
                print("\n❌ Password cannot be empty")
                return
            
            request = {
                "type": "signup",
                "username": username,
                "display_name": display_name,
                "password": password
            }
            
            print("\nProcessing signup request...")
            await self.send_request(request)

        except Exception as e:
            print(f"\nError during signup: {e}")

    async def show_menu(self):
        while self.running:
            try:
                print("\n=== Authentication Menu ===")
                print("1. Login")
                print("2. Sign Up")
                print("3. Exit")
                choice = input("\nChoose an option: ")
                
                if choice == "1":
                    await self.login()
                elif choice == "2":
                    await self.signup()
                elif choice == "3":
                    print("\nGoodbye! 👋")
                    self.running = False
                    if self.websocket:
                        await self.websocket.close()
                    break
                else:
                    print("\n❌ Invalid option. Please try again.")
                    continue
                    
                # Wait for user input before showing menu again
                if choice in ["1", "2"]:
                    input()  # Wait for Enter key

            except Exception as e:
                print(f"\n❌ Error in menu: {e}")
                break

    async def start(self):
        if await self.connect():
            # Start receiving messages in background
            receive_task = asyncio.create_task(self.receive_messages())
            
            try:
                # Show menu and handle user input
                await self.show_menu()
            finally:
                # Clean up
                receive_task.cancel()
                try:
                    await receive_task
                except asyncio.CancelledError:
                    pass
                
                if self.websocket:
                    await self.websocket.close()
        else:
            print("Failed to connect to server")

def main():
    try:
        client = AuthenticationClient()
        asyncio.run(client.start())
    except KeyboardInterrupt:
        print("\nClient shutting down... 👋")
    except Exception as e:
        print(f"\nUnexpected error: {e}")
    finally:
        sys.exit(0)

if __name__ == "__main__":
    main()