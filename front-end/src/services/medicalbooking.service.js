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
    return axios.get(BASE_URL + "api/training/management/dzongkhagHospitalMappings/getAllActiveHospitalsByDzongkhagId"
        , {
            params: { dzongkhagId }
            , headers: authHeader()
        });
};

const getAllAvailableAppointmentDateByHospitalId = (hospitalId) => {
    return axios.get(BASE_URL + "api/medical/screening/hospitalScheduleDate/getAllAvailableAppointmentDateByHospitalId"
        , {
            params: {
                hospitalId
            }
            , headers: authHeader()
        });
};

const bookMedicalAppointment = (data) => {
    return axios.post(BASE_URL + "api/medical/screening/medicalBooking/bookMedicalAppointment"
        , data
        , {
            headers: authHeader()
        }
    );
};

const editMedicalAppointment = (data) => {
    return axios.post(BASE_URL + "api/medical/screening/medicalBooking/editMedicalAppointment"
        , data
        , {
            headers: authHeader()
        }
    );
};

export default {
    getAllMedicalQuestion
    , getAllDzongkhag
    , getAllActiveHospitalsByDzongkhagId
    , getAllAvailableAppointmentDateByHospitalId
    , bookMedicalAppointment
    , editMedicalAppointment
};