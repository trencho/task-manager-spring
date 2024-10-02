db = db.getSiblingDB("task-manager");

db.createUser({
    user: "admin",
    pwd: "$2y$10$LGDcpci0mI.Jihj9FI1b1OzsDx7l.NgFJrgKECBNWPRY80qRb20fC",
    roles: [
        {
            role: "readWrite",
            db: "task-manager"
        }
    ],
    mechanisms: ["SCRAM-SHA-256"]
});

db.createUser({
    user: "username",
    pwd: "$2y$10$LGDcpci0mI.Jihj9FI1b1OzsDx7l.NgFJrgKECBNWPRY80qRb20fC",
    roles: [
        {
            role: "readWrite",
            db: "task-manager"
        }
    ],
    mechanisms: ["SCRAM-SHA-256"]
});

db.createCollection("users");
db.users.insertOne({
    username: "username",
    password: "$2y$10$LGDcpci0mI.Jihj9FI1b1OzsDx7l.NgFJrgKECBNWPRY80qRb20fC",
    roles: ["USER"]
});

db.createCollection("tasks");

print("MongoDB initialization script executed successfully.");
