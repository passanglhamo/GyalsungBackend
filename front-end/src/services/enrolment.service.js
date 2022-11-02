import axios from "axios";

const BASE_URL = process.env.REACT_APP_BASE_URL_EDE + "enrolment";
const save = (data) => {
    return axios.post(BASE_URL + "/save", data,
        {
            headers: { "Content-Type": "multipart/form-data" }
        }
    );
};

const downloadParentConsentForm = (url) => {
    return axios.get(BASE_URL + "/downloadParentConsentForm", { params: { url }, responseType: 'blob' })
};

export default {
    save,
    downloadParentConsentForm,
};