import psycopg2

# check db connection
connection = psycopg2.connect(
    host="localhost",
    database="postgres",
    user="postgres",
    password="ahmed"
)

cur=connection.cursor()

cur.execute("select * from authentication")
result=cur.fetchall()

print("All User Data :")
print("--UserName--DisplayName--Password--Blocked")
print("===========================================")
print(result)