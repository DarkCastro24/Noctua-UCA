const News = require("../models/news.model"); 

const controller = {}

controller.saveNews = async(req, res, next) => {
    try {
        const {tittle,body,date,images,link} = req.body
        const news = new News()
        news["tittle"] = tittle
        news["body"] = body
        news["date"] = date
        news["images"] = images
        news["link"] = link
        const newsSave = await news.save()
        if(!newsSave){
            return res.status(401).json({error: "error saving news"})
        }
        return res.status(201).json(newsSave)
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Server internal error', message: error.message }); 
    }
}

controller.deleteNews = async(req, res, next) => {
    try {
        const {identifier} = req.params
        const news = await News.findOneAndDelete({_id: identifier})
        if(!news){
            return res.status(401).json({error: "error deleting news"})
        }
        return res.status(201).json({message : "news deleated sucsesfully"})
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Server internal error', message: error.message }); 
    }
}

controller.hideNews = async(req, res, next) => {
    try {
        const {identifier}= req.params
        const news = await News.findOne({_id: identifier})
        if(!news){
            return res.status(401).json({error: "error finding news"})
        }
        news.hidden = !news.hidden 
        const newNews = await news.save();
        if(!newNews){
            return res.status(401).json({error: "error saving modification"})
        }
        return res.status(201).json({newNews})
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Server internal error', message: error.message }); 
    }
}

controller.findNews = async(req, res, next) => {
    try {
        const news = await News.find({hidden: false})
        if(!news){
            return res.status(401).json({error: "error finding news"})
        }
        return res.status(201).json(news)
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Server internal error', message: error.message }); 
    }
}

controller.findNewsAdmin = async(req, res, next) => {
    try {
        const news = await News.find()
        if(!news){
            return res.status(401).json({error: "error finding modificacion"})
        }
        return res.status(201).json({news})
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Server internal error', message: error.message }); 
    }
}
module.exports = controller