import pika, json, time

# Connect to RabbitMQ
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

# Declare queue (durable ensures persistence)
channel.queue_declare(queue='library_books', durable=True)

books = [
    {"book_id": "B001", "title": "Operating Systems", "author": "Silberschatz", "publisher": "Wiley"},
    {"book_id": "B002", "title": "Distributed Systems", "author": "Tanenbaum", "publisher": "Pearson"},
    {"book_id": "B003", "title": "Database Systems", "author": "Elmasri", "publisher": "Pearson"}
]

for book in books:
    channel.basic_publish(
        exchange='',
        routing_key='library_books',
        body=json.dumps(book),
        properties=pika.BasicProperties(delivery_mode=2)  # persistent
    )
    print(f" [x] Library staff added book: {book['title']}")
    time.sleep(1)

connection.close()
