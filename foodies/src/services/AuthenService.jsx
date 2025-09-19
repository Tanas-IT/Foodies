import React from "react";
import { API_BASE_URL } from "../config/api";
import axios from "axios";

export const register = async (data) => {
  try {
    const response = await axios.post(
      `${API_BASE_URL}/api/user/register`,
      data
    );
    return response;
  } catch (error) {
    console.log("Error fetching food details:", error);
    throw error;
  }
};

export const login = async (data) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/api/user/login`, data);
    return response;
  } catch (error) {
    console.log("Error fetching food details:", error);
    throw error;
  }
};
