import axios from "axios";
import authHeader from "./auth-header";

// const BASE_URL = "http://localhost:8080/hospitalScheduleTime";
const BASE_URL = process.env.REACT_APP_BASE_URL + "api/medical/screening/hospitalScheduleDate";
// const BASE_URL_TIMES = "http://localhost:8080/hospitalScheduleTimeLists";
const BASE_URL_TIMES = process.env.REACT_APP_BASE_URL + "api/medical/screening/hospitalScheduleTimes";
const save = (formData) => {
    return axios.post(BASE_URL, formData,
        {
            headers: authHeader()
        }
    );
};

const getAll = () => {
    return axios.get(BASE_URL,
        {
            headers: authHeader()
        });
};

const getAllById = (id) => {
    return axios.get(BASE_URL + "/" + id,
        {
            headers: authHeader()
        });
};

const update = (id, formData) => {
    return axios.put(BASE_URL + "/" + id, formData,
        {
            headers: authHeader()
        }
    );
};

const getAllScheduleTimesById = (dzoHosId) => {
    return axios.get(BASE_URL+"/getAllScheduleTimesById",
        {
            params: {
                dzoHosId
            }
            ,headers: authHeader()
        });
};

const updateScheduleTimes = (formData) => {
    return axios.post(BASE_URL + "/updateScheduleTimes" ,formData,
        {
            headers: authHeader()
        }
    );
};

const deleteScheduleTimes = (id) => {
    return axios.delete(BASE_URL_TIMES + "/" + id,
        {
            headers: authHeader()
        });
};

export default {
    save,
    getAll,
    getAllById,
    update,
    getAllScheduleTimesById,
    updateScheduleTimes,
    deleteScheduleTimes
};