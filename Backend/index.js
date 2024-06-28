const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const database = require('./config/database.config');
const cors = require('cors');
const apiRouter = require('./routes/index.router'); // Asegúrate de que la ruta es correcta

const app = express();

async function startServer() {
    // Middleware
    app.use(cors());
    app.use(logger('dev'));
    app.use(express.json());
    app.use(express.urlencoded({ extended: false }));
    app.use(cookieParser());
    app.use(express.static(path.join(__dirname, 'public')));
    app.use('/api', apiRouter); // Asegúrate de que el prefijo /api es correcto

    try {
        await database.connect();
        console.log("Database connected successfully.");

        const port = process.env.PORT || 3500;
        app.listen(port, () => {
            console.log(`Server running on port ${port}`);
        });
    } catch (err) {
        console.error("Database connection failed:", err);
        process.exit(1);
    }
}

startServer();

module.exports = app;


