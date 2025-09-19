import axios from "axios";
import { API_BASE_URL } from "../config/api";
import { toast } from "react-toastify";

export const addFood = async (foodData, image) => {
  const formData = new FormData();
  formData.append("food", JSON.stringify(foodData));
  formData.append("file", image);

  try {
    await axios.post(`${API_BASE_URL}/api/foods`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  } catch (error) {
    console.log("Error", error);
    throw error;
  }
};

export const getListFood = async (pageIndex) => {
  try {
    const response = await axios.get(
      `${API_BASE_URL}/api/foods?pageIndex=${pageIndex}`
    );
    return response;
  } catch (error) {
    console.log("Error fetching food list", error);
    throw error;
  }
};

export const deleteFood = async (foodId) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/api/foods/${foodId}`);
    return response;
  } catch (error) {
    toast.error("Error while deleting the food.");
    throw error;
  }
};
