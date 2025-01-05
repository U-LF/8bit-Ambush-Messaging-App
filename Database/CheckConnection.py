import psycopg2

# check db connection
connection = psycopg2.connect(
    host="localhost",
    database="postgres",
    user="postgres",
    password="ahmed"
)

print("Connected to the database successfully!")
connection.close()
