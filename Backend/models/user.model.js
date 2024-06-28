const mongoose = require('mongoose');
const validator = require('validator');
const crypto = require('crypto');
const debug = require('debug')('app:user-model');

const userSchema = new mongoose.Schema({
    username: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    name: {
        type: String,
        required: true,
        trim: true
    },
    type: {
        type: Number,
        enum: [0, 1],
        default: 1 // 0: admin, 1: student
    },
    career: {
        type: String,
        trim: true
    },
    phone: {
        type: String,
        validate(value) {
            if (!validator.isMobilePhone(value, 'any', { strictMode: false })) {
                throw new Error('Invalid phone number.');
            }
        }
    },
    email: {
        type: String,
        lowercase: true,
        trim: true,
    },
    biography: {
        type: String,
        trim: true
    },
    hobbies: {
        type: String
    },
    currentSubjects: [{
        type: String,
        trim: true
    }],
    allSubjects: [{
        type: String,
        trim: true
    }],
    hashedPassword: {
        type: String
    },
    profilePhoto: {
        type: String
    },
    visible:{
        type: String,
        default: "true"
    },
    salt: {
        type: String
    },
    tokens: {
        type: [String],
        default: []
    }
}, { timestamps: true });

userSchema.methods = {
    encryptPassword: function (password) {
        if (!password) return '';
        try {
            const _password = crypto.pbkdf2Sync(password, this.salt, 1000, 64, 'sha512').toString('hex');
            return _password;
        } catch (error) {
            debug({ error });
            return '';
        }
    },
    makeSalt: function () {
        return crypto.randomBytes(16).toString('hex');
    },
    comparePassword: function (password) {
        return this.hashedPassword === this.encryptPassword(password);
    }
};

userSchema
    .virtual('password')
    .set(function (password = crypto.randomBytes(16).toString()) {
        this.salt = this.makeSalt();
        this.hashedPassword = this.encryptPassword(password);
    });

module.exports = mongoose.model('User', userSchema);

