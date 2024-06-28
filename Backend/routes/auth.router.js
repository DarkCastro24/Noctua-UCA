const express = require("express");
const router = express.Router();

const authController = require("../controllers/auth.controller");

// http://localhost:3500/api/auth... 

// server/api/auth/register
router.post("/register", 
    authController.register
);

// server/api/auth/login
router.post("/login", 
    authController.login
);

module.exports = router;
