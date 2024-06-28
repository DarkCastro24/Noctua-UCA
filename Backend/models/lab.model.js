const Mongoose = require("mongoose");
const Schema = Mongoose.Schema;

const ScheduleSchema = new Schema({
    date: {
        type: String,
        required: true
    },
    hour: {
        type: String,
        required: true
    },
    activity: {
        type: String
    },
    available: {
        type: Boolean,
        default: true
    }
});

const LabSchema = new Schema({
    labnumber: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    alumAmount: {
        type: Number,
        required: true
    },
    urlImage:{
        type: String,
        required: true
    },
    schedule: [ScheduleSchema]
}, { timestamps: true });

module.exports = Mongoose.model("Laboratory", LabSchema);

