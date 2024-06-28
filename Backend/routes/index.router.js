const express = require("express");
const router = express.Router();

// Creamos un router por cada archivo dentro de routes 
const userRouter = require('./user.router');
//const mapRouter = require("./map.router")
const labRouter = require('./lab.router')
const authRouter = require('./auth.router');
const newsRouter = require('./news.router');

// Por cada router asignar una ruta para realizar las peticiones
router.use("/auth", authRouter);
router.use("/user", userRouter);
//router.use("/map", mapRouter)
router.use("/lab", labRouter)
router.use("/news", newsRouter)


router.get('/test', (req, res) => {
    res.json({ message: 'Test route works!' });
});


module.exports = router;