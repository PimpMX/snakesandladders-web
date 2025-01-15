// src/firebase.js
import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";

// Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyC5V5Lyr9MKE6PCSjwq6ADRhAzjOb65aYI",
    authDomain: "snakes-and-ladders-web.firebaseapp.com",
    projectId: "snakes-and-ladders-web",
    storageBucket: "snakes-and-ladders-web.firebasestorage.app",
    messagingSenderId: "209528630302",
    appId: "1:209528630302:web:3e61224fe8c82d7372920f",
    measurementId: "G-XC1BYQBWXB",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

// Google login function
export const loginWithGoogle = async () => {
    try {
        const result = await signInWithPopup(auth, provider);
        return result.user; // Authenticated user
    } catch (error) {
        console.error("Google Login Error:", error);
        throw error;
    }
};
