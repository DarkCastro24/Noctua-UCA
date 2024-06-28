const express = require("express");
const router = express.Router();

const controller = require("../controllers/user.controller");

// http://localhost:3500/api/user... 

// server/api/user/search 
router.get('/search/', 
    controller.find);

// server/api/user
router.get("/", 
    controller.findAll);

// server/api/user/id  
router.get("/:id", 
    controller.getOne);

// server/api/user/id  
router.patch("/:id", 
    controller.update);

// server/api/user/id  
router.delete("/:id", 
    controller.delete);

// server/api/user/password/id  
router.put("/password/:id", 
    controller.changePassword);

router.put('/:id/subjects', 
    controller.updateSubjects);

module.exports = router;
