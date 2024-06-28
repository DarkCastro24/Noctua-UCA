const express = require("express");
const router = express.Router();
const controller = require("../controllers/news.controller");

router.post(["/:identifier", "/"], controller.saveNews);
router.get('/', controller.findNews);
router.get('/admin', controller.findNewsAdmin);
router.delete("/:identifier", controller.deleteNews)
router.patch('/:identifier', controller.hideNews);

module.exports = router