import axios from "axios";
import { API_BASE_URL } from "../config/api";

export const getAllFood = async (pageIndex) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/api/foods?${pageIndex}`);
    return response;
  } catch (error) {
    console.log("Error fetching food list:", error);
    throw error;
  }
};

export const getDetailFood = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/api/foods/${id}`);
    return response;
  } catch (error) {
    console.log("Error fetching food details:", error);
    throw error;
  }
};
