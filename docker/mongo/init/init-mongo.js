db = db.getSiblingDB("task-manager"); // Create or switch to the "task-manager" database

db.createUser({
    user: "admin",
    pwd: "password", // replace with your password
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
    pwd: "password", // replace with your password
    roles: [
        {
            role: "readWrite",
            db: "task-manager"
        }
    ],
    mechanisms: ["SCRAM-SHA-256"]
});

// Create a "users" collection and insert a sample user
db.createCollection("users");
db.users.insertOne({
    username: "username",
    password: "password",
    roles: ["USER"]
});

// Create a "tasks" collection and insert some sample tasks
db.createCollection("tasks");
db.tasks.insertMany([
    {
        title: "Task 1",
        description: "Description for task 1",
        completed: false
    },
    {
        title: "Task 2",
        description: "Description for task 2",
        completed: true
    }
]);

print("MongoDB initialization script executed successfully.");
