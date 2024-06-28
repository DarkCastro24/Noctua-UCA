const User = require("../models/user.model");

const controller = {};

controller.register = async (req, res, next) => {
    try {
        const {username, name, password, profilePhoto, type} = req.body;
        const user = await User.findOne({$or: [{username: username}]});
        if (user) {
            // Usuario ya registrado (se retorna todo el registro)
            return res.status(201).json({message: "Encontrado" ,user}); 
        } else {
            // No se encuentra registrado se registra con la informacion proporcionada
            const newUser = new User({
                username: username,
                password: password,
                name: name,
                email: username, 
                profilePhoto: profilePhoto,
                type: type
            })
            await newUser.save();
            return res.status(201).json({message: "Registrado" ,id: newUser._id, name: newUser.name, username: newUser.username, profilePhoto: newUser.profilePhoto});
        }
    } catch (error) {
        console.error(error);
        return res.status(500).json({error: "Server internal error"});        
    }
}

controller.login = async (req, res, next) => {
    try {
        const { username, password } = req.body;
        const user = await User.findOne({$or: [{username: username}/*,{email: username}*/] });
        // Si no existe retornar 404
        if (!user) {
            return res.status(404).json({error: "Usuario no encontrado"});
        }
        // Si la password no coincide -- 401
        if (!user.comparePassword(password)) {
            return res.status(401).json({error: "Contraseña incorrecta"});
        }
        return res.status(200).json({user});

    } catch (error) {
        console.error(error);
        return res.status(500).json({error: "Internal Server Error"});        
    }
}

module.exports = controller;