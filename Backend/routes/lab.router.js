const express = require("express");
const router = express.Router();
const {crearLab} = require("../validators/lab.validator")
const validatefields = require("../middlewares/index.middlewares")
const controller = require("../controllers/lab.controller");

router.post(["/:identifier", "/"],crearLab,validatefields, controller.save);
router.get('/', controller.findall);
router.delete("/:identifier", controller.delete)
router.patch('/:identifier/schedule/:scheduleId', controller.updateSchedule);
router.put('/:identifier', controller.update);

module.exports = router