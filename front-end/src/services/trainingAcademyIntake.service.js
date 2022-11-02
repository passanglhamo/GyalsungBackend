import axios from "axios";
import authHeader from "./auth-header";

// const BASE_URL = "http://localhost:8080/trainingAcademyCapacities";
const BASE_URL = process.env.REACT_APP_BASE_URL + "api/training/management/trainingAcademyCapacities";
const save = (formData) => {
    return axios.post(BASE_URL , formData,
        {
            headers: authHeader()
        }
    );
};

const getAll = () => {
    return axios.get(BASE_URL,
        { headers: authHeader() });
};

const getAllById = (id) => {
    return axios.get(BASE_URL + "/getAllTrainingAcaCapById",
        { params: {id}, headers: authHeader()});
};

const update = (id,formData) => {
    return axios.put(BASE_URL+"/"+id , formData,
        {
            headers: authHeader()
        }
    );
};

export default {
    save,
    getAll,
    getAllById,
    update
};