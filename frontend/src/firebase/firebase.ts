import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";

const firebaseConfig = {
  apiKey: "AIzaSyBKCg1jSOSiBkwxaRWFJRIPT3XAnyfCsbo",
  authDomain: "smart-peso-a5238.firebaseapp.com",
  projectId: "smart-peso-a5238",
  storageBucket: "smart-peso-a5238.appspot.com",
  messagingSenderId: "984598753915",
  appId: "1:984598753915:web:ef4542aab4af1f399068de",
  measurementId: "G-0V8P8GV1TJ"
};

export const app = initializeApp(firebaseConfig);
export const analytics = getAnalytics(app);
