const Lab = require("../models/lab.model");
const controller = {};

module.exports = controller;

controller.save = async (req, res, next) => {
    try {
        const { identifier } = req.params;
        let lab = await Lab.findById(identifier);
        if (!lab) {
            lab = new Lab();
        }
        const { labnumber, description, alumAmount, urlImage, schedule } = req.body;

        lab.labnumber = labnumber;
        lab.description = description;
        lab.alumAmount = alumAmount;
        lab.urlImage = urlImage;
        lab.schedule = schedule.map(item => ({
            date: item.date,
            hour: item.hour,
            activity: item.activity,
            available: item.available !== undefined ? item.available : true
        }));

        const labsave = await lab.save();
        if (!labsave) {
            return res.status(401).json({ error: "Error creating Lab" });
        }
        return res.status(201).json(labsave);
    } catch (error) {
        console.error(error);
        return res.status(500).json({ error: 'Internal Server Error' });
    }
};

controller.findall = async (req, res, next) => {
    try {
        const labs = await Lab.find();
        return res.status(201).json(labs);
    } catch (error) {
        console.error(error);
        return res.status(500).json({ error: 'Internal Server Error' });
    }
};

controller.delete = async (req, res, next) => {
    try {
        const { identifier } = req.params;
        const lab = await Lab.findOneAndDelete({ _id: identifier });
        if (!lab) {
            return res.status(401).json({ error: "Error finding Lab" });
        }
        return res.status(201).json({ message: "Lab has been eliminated" });
    } catch (error) {
        console.error(error);
        return res.status(500).json({ error: 'Internal Server Error' });
    }
};

controller.updateSchedule = async (req, res, next) => {
    try {
        const { identifier, scheduleId } = req.params;
        const updates = req.body;

        let lab = await Lab.findById(identifier);
        if (!lab) {
            return res.status(404).json({ error: "Lab not found" });
        }

        let scheduleItem = lab.schedule.id(scheduleId);
        if (!scheduleItem) {
            return res.status(404).json({ error: "Schedule not found" });
        }

        Object.keys(updates).forEach(key => {
            scheduleItem[key] = updates[key];
        });

        const updatedLab = await lab.save();
        return res.status(200).json(updatedLab);
    } catch (error) {
        console.error(error);
        return res.status(500).json({ error: 'Internal Server Error' });
    }
};

controller.update = async (req, res, next) => {
    try {
        const { identifier } = req.params;
        const updates = req.body;
        
        let lab = await Lab.findById(identifier);
        if (!lab) {
            return res.status(404).json({ error: "Lab not found" });
        }
        
        Object.keys(updates).forEach(key => {
            lab[key] = updates[key];
        });
        
        const updatedLab = await lab.save();
        return res.status(200).json(updatedLab);
    } catch (error) {
        console.error(error);
        return res.status(500).json({ error: 'Internal Server Error' });
    }
};
