import axios from "axios";
import authHeader from "./auth-header";
import authHeaderMultipart from "./auth-header-multupart";

const BASE_URL = process.env.REACT_APP_BASE_URL;

const getAllMedicalQuestion = () => {
    return axios.get(BASE_URL + "api/training/management/medicalQuestionnaires"
        , { headers: authHeader() });
};
const getAllDzongkhag = () => {
    return axios.get(BASE_URL + "api/training/management/common/getAllDzongkhags"
        , { headers: authHeader() });
};

const getAllActiveHospitalsByDzongkhagId = (dzongkhagId) => {
    return axios.get(BASE_URL + "api/training/management/hospitalScheduleTime/getAllActiveHospitalsByDzongkhagId"
        , {
            params: { dzongkhagId }
            , headers: authHeader()
        });
};

const getAllAvailableTimeSlotByHospitalId = (hospitalId) => {
    return axios.get(BASE_URL + "api/training/management/hospitalScheduleTime/getAllAvailableTimeSlotByHospitalId"
        , {
            params: {
                hospitalId
            }
            , headers: authHeader()
        });
};
const bookMedicalAppointment = (dzongkhagId, mQuestionDtoList) => {
    return axios.post("http://localhost:8086/api/training/management/common/bookMedicalAppointment"
        , { dzongkhagId, mQuestionDtoList }
        , {
            headers: authHeaderMultipart()
        });
};

export default {
    getAllMedicalQuestion
    , getAllDzongkhag
    , getAllActiveHospitalsByDzongkhagId
    , getAllAvailableTimeSlotByHospitalId
    , bookMedicalAppointment
};