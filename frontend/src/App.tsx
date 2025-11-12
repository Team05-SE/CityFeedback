import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { Routes, Route, Link } from 'react-router-dom'
import SignupPage from "@/app/signup/page"
import LoginPage from "@/app/login/page"

function HomePage() {
  return (
    <div className="flex min-h-screen items-center justify-center">
      <div className="text-center space-y-4">
        <h1 className="text-3xl font-bold">Welcome to CityFeedback</h1>
        <div className="flex gap-4 justify-center">
          <Link
            to="/signup"
            className="bg-primary text-primary-foreground px-4 py-2 rounded-md hover:opacity-90"
          >
            Sign Up
          </Link>
          <Link
            to="/login"
            className="border border-border px-4 py-2 rounded-md hover:bg-accent"
          >
            Login
          </Link>
        </div>
      </div>
    </div>
  )
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/login" element={<LoginPage />} />
    </Routes>
  )
}

export default App
