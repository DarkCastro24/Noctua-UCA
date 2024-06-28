const { body } = require("express-validator");

const validators = {};

validators.crearLab = [
    body("labnumber")
        .notEmpty().withMessage("lab number is a required field")
        .isString().withMessage("lab number must be a string"),
    body("description")
        .notEmpty().withMessage("description is a required field")
        .isString().withMessage("description must be a string"),
    body("alumAmount")
        .notEmpty().withMessage("alumni amount is a required field")
        .isInt({ min: 0 }).withMessage("alumni amount must be a non-negative integer"),
    body("schedule")
        .isArray().withMessage("schedule must be an array"),
    body("schedule.*.date")
        .notEmpty().withMessage("schedule date is a required field")
        .isISO8601().withMessage("invalid schedule date"),
    body("schedule.*.hour")
        .notEmpty().withMessage("schedule hour is a required field")
        .isString().withMessage("schedule hour must be a string"),
    body("schedule.*.activity")
        .optional()
        .isString().withMessage("schedule activity must be a string"),
    body("schedule.*.available")
        .optional()
        .isBoolean().withMessage("schedule available must be a boolean")
];

module.exports = validators;
