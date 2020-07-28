require("dotenv").config();
const express = require("express");
const app = express();
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const { MongoClient } = require("mongodb");
const connectionString =
  "mongodb+srv://user:soSmart@101@cluster0-olorv.mongodb.net/<dbname>?retryWrites=true&w=majority";
MongoClient.connect(connectionString, { useUnifiedTopology: true })
  .then((client) => {
    console.log("Connected to Database");
    const db = client.db("jobs");
    app.get("/users", (req, res) => {
      res.send("Home");
    });

    app.get("/products", (req, res) => {
      db.collection("products")
        .find()
        .toArray()
        .then((result) => res.status(200).send(result));
    });

    app.post("/products", authenticateToken, (req, res) => {
      //console.log(req);
      if (req.user != null) {
        // ADD Products
        if (req.query.type == "ADD") {
          db.collection("products").insertOne(req.body);
          res.status(200).send("Added");
        }

        // DELETE Products
        if (req.query.type == "DELETE") {
          console.log(req.body.name);
          db.collection("products").deleteOne({ name: req.body.name });
          res.status(200).send("Deleted");
        }
      }
    });

    app.post("/users", async (req, res) => {
      try {
        const hashedPassword = await bcrypt.hash(req.body.password, 10);
        const user = { name: req.body.name, password: hashedPassword };
        db.collection("users").insertOne(user);
        res.status(201).send();
      } catch {
        res.status(500).send();
      }
    });

    app.post("/users/login", async (req, res) => {
      db.collection("users").findOne(
        { name: req.body.name },
        async (err, result) => {
          if (err) throw err;
          if (result == null) res.status(400).send("User doesn't exist");
          else {
            try {
              if (await bcrypt.compare(req.body.password, result.password)) {
                const accessToken = jwt.sign(
                  { name: req.body.name },
                  process.env.ACCESS_TOKEN_SECRET
                );
                res.send({
                  message: "Successfully Authenticated",
                  accessToken: accessToken,
                });
              } else {
                res.send("Authentication Failed");
              }
            } catch {
              res.status(500).send();
            }
          }
        }
      );
    });
  })
  .catch((err) => console.log(err));

function authenticateToken(req, res, next) {
  const authHeader = req.headers["authorization"];
  const token = authHeader && authHeader.split(" ")[1];
  if (token == null) res.sendStatus(401);
  jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, user) => {
    if (err) return res.sendStatus(403);
    req.user = user;
    next();
  });
}

app.use(express.json());

app.listen(3000);
