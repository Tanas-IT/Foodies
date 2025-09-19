import React, { useState } from "react";
import { Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import Login from "./pages/Login/Login";
import MainLayout from "./pages/Layouts/MainLayout";

const App = () => {
  return (
    <>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/*" element={<MainLayout />} />
      </Routes>

      <ToastContainer position="top-right" autoClose={3000} />
    </>
  );
};

export default App;
