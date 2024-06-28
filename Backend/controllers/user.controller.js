const User = require("../models/user.model");

const controller = {};

// Método para obtener todos los usuarios
controller.findAll = async (req, res, next) => {
    try {
        const users = await User.find();
        return res.status(200).json({ users });
    } catch (error) {
        console.error(error);
        return res.status(500).json({ error: 'Server internal error', error: error.message});
    }
}

// Método para obtener un solo usuario por ID o nombre de usuario
controller.getOne = async (req, res, next) => {
    try {
        const username = req.params.id;  
        let user;
        // Valida si el parámetro es un ObjectId de MongoDB
        if (username.match(/^[0-9a-fA-F]{24}$/)) { 
            user = await User.findById(username);
        } else {
            user = await User.findOne({ username: username }); 
        }
        if (!user) {
            return res.status(404).send({ message: "Usuario no encontrado" }); 
        }
        res.status(200).json(user); 
    } catch (error) {
        console.error(error);
        res.status(500).send({ message: "Server internal error", error: error.message }); 
    }
};

// Método para actualizar un usuario
controller.update = async (req, res) => {
    const id = req.params.id; // Extrae el ID del usuario de los parámetros
    try {
        const update = await User.findByIdAndUpdate(id, req.body, { new: true });
        if (!update) {
            return res.status(404).send({ message: "Usuario no encontrado" }); 
        }
        res.status(200).json(update);
    } catch (error) {
        console.error(error);
        res.status(400).json({ message: "Server internal error", error: error.message }); 
    }
};

// Método para eliminar un usuario
controller.delete = async (req, res) => {
    try {
        const id = req.params.id; 
        const result = await User.findByIdAndDelete(id);
        if (!result) {
            return res.status(404).send({ message: "Usuario no encontrado" }); 
        }
        res.status(200).send({ message: "Usuario eliminado exitosamente" }); 
    } catch (error) {
        console.error(error);
        res.status(500).send({ message: "Server internal error", error: error.message }); 
    }
};

// Método para buscar usuarios basándose en una cadena en su nombre o materias
controller.find = async (req, res, next) => {
    try {
        const data = req.query.data; 
        const regex = new RegExp(data, 'i'); 

        let users = await User.find({ name: regex });
        
        if (users.length === 0) {
            users = await User.find({ subjects: regex });
        }

        res.status(200).json(users); 
    
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: "Server internal error", error: error.message }); 
    }
}

controller.changePassword = async (req, res, next) => {
    const id = req.params.id;
    const { currentPassword, password: newPassword } = req.body;

    if (!newPassword || !currentPassword) {
        return res.status(400).send({ message: "Ambas contraseñas son requeridas" });
    }

    try {
        const user = await User.findById(id);
        if (!user) {
            return res.status(404).send({ message: "Usuario no encontrado" });
        }

        if (!user.comparePassword(currentPassword)) {
            return res.status(400).send({ message: "La contraseña actual no es correcta" });
        }

        user.password = newPassword; 
        await user.save();

        res.status(200).send({ user});
    } catch (error) {
        console.error(error);
        res.status(500).send({ message: "Error interno del servidor", error: error.message });
    }
};

controller.updateSubjects = async (req, res) => {
    const id = req.params.id; // Extrae el ID del usuario de los parámetros
    const { currentSubjects, allSubjects } = req.body;

    if (!currentSubjects && !allSubjects) {
        return res.status(400).send({ message: "Debe proporcionar currentSubjects o allSubjects para actualizar" });
    }

    try {
        const user = await User.findById(id);
        if (!user) {
            return res.status(404).send({ message: "Usuario no encontrado" });
        }

        if (currentSubjects) {
            user.currentSubjects = currentSubjects;
        }
        if (allSubjects) {
            user.allSubjects = allSubjects;
        }

        await user.save();
        res.status(200).json(user);
    } catch (error) {
        console.error(error);
        res.status(500).send({ message: "Server internal error", error: error.message });
    }
};

module.exports = controller;