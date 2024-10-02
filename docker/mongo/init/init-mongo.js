db = db.getSiblingDB("task-manager");

db.createUser({
    user: "admin",
    pwd: "password",
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
    pwd: "password",
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
    password: "password",
    roles: ["USER"]
});

db.createCollection("tasks");

print("MongoDB initialization script executed successfully.");
