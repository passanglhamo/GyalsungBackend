import React, {useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import * as Yup from "yup";
import {Formik} from "formik";
import FormButton from "../FormsUI/Button";
import hospitalScheduleTimeService from "../../services/hospitalScheduleTime.service";
import dzongkhagHospitalMapService from "../../services/dzongkhagHospitalMap.service";
import TextField from "../FormsUI/Textfield";
import {MenuItem} from "@material-ui/core";
import DateTimePicker from "../FormsUI/DateTimePicker";
import {KeyboardTimePicker, MuiPickersUtilsProvider} from '@material-ui/pickers';
import IconButton from '@mui/material/IconButton';
import AddBoxIcon from '@mui/icons-material/AddBox';
import DeleteIcon from '@mui/icons-material/Delete';
import DateFnsUtils from "@date-io/date-fns";
import {Alert, Snackbar} from "@mui/material";
import hospitalService from "../../services/hospital.service";

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        padding: theme.spacing(2),

        '& .MuiTextField-root': {
            margin: theme.spacing(1),
            width: '100%',
        },
        '& .MuiButtonBase-root': {
            margin: theme.spacing(2),
        },
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: '100%',
    },
}));
const HospitalScheduleTime = () => {

    /*================ constants value =============*/
    const classes = useStyles();
    const [content, setContent] = useState('');
    const [dzoHospitalMappingList, setDzoHospitalMappingList] = useState([]);
    const [appointmentStartTime, setAppointmentStartTime] = useState([null]);
    const [appointmentEndTime, setAppointmentEndTime] = useState([null]);
    const [inputList, setInputList] = useState([{startTime: null, endTime: null}]);
    const [hospitalId, setHospitalId] = useState(null);
    const [openSuccess, setOpenSuccess] = useState(false);
    const [hospitalList, setHospitalList] = useState([]);
    const [activeStatus, setActiveStatus] = useState('A');



    const handleHospital = (items) => {
        setHospitalId(items.hospitalId);
    };

    const handleCloseSuccess = () => {
        setOpenSuccess(false);
    };

    const resetPage = (resetForm) => {
        resetForm();
        setOpenSuccess(true);

    };
    /*================ Initial value for the fields =============*/
    const INITIAL_FORM_STATE = {
        hospitalId: '',
        appointmentDate: null,
        //status: ''

    };

    /*================ Get All Hospital method =============*/
    const getHospitalList = () => {
        hospitalService.getAll().then(
            response => {
                setHospitalList(response.data)
            },
            error => {
                setContent(
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString()
                );
            }
        );

    }

    /*================ Get All Hospital method =============*/
    const getDzoHosMapList = () => {
        dzongkhagHospitalMapService.getAllByStatus(activeStatus).then(
            async response => {
                setDzoHospitalMappingList(response.data);
            },
            error => {
                setContent(
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString()
                );
            }
        );
    }

    /*================ form validation =============*/
    const FORM_VALIDATION = Yup.object().shape({
        hospitalId: Yup.string()
            .required('Hospital is required'),
        appointmentDate: Yup.date()
            .required('Appointment Date is required')
            .typeError("Appointment Date is required"),
        // startTime: Yup.date()
        //     .required('Required'),
        // endTime: Yup.date()
        //     .required('Required'),
        // status: Yup.string()
        //     .required('Required'),
    });


    /*================ Use Effect =============*/

    useEffect(() => {
        getHospitalList();
        getDzoHosMapList();
    }, [])


    /*================ Submit method =============*/
    const submitForm = (fields,{setSubmitting, resetForm}) => {
        let hospitalScheduleTimeList = inputList;
        let appointmentDate = fields.appointmentDate;
        const data = {hospitalId, appointmentDate, hospitalScheduleTimeList};
        hospitalScheduleTimeService.save(data)
            .then(() => {
                resetPage(resetForm);
            })
            .catch(() => {
                setSubmitting(false);
            });
    }

    /*================ Handle approve and reject method =============*/
    const handleStartTime = (time, index) => {
        const list = [...inputList];
        list[index]["startTime"] = time;
        appointmentStartTime[index] = time;
        setInputList(list);
    };
    const handleEndTime = (time, index) => {
        const list = [...inputList];
        list[index]["endTime"] = time;
        appointmentEndTime[index] = time;
        setInputList(list);
    };
    const handleAddMore = () => {

        setInputList([...inputList, {startTime: null, endTime: null}])
    };

    const handleRemove = index => {
        const list = [...inputList];
        list.splice(index, 1);
        setInputList(list);
    };


    return (
        <>
            <Formik initialValues={{...INITIAL_FORM_STATE}}
                    validationSchema={FORM_VALIDATION}
                    onSubmit={submitForm}>
                {({values, setFieldValue}) => {
                    return (
                        <form className={classes.root}>
                            <div className='mb-2'>
                                <h4><FontAwesomeIcon className="text-danger"/> Hospital Time Schedule Setup </h4>
                            </div>
                            <div className="col-md-12 row">
                                <div className="col-md-4  row">
                                    <TextField select={true} variant='standard' required name="hospitalId"
                                               fullWidth={true} label="Hospital">
                                        {dzoHospitalMappingList.map((items) => {
                                            return (
                                                <MenuItem onClick={(e) => handleHospital(items)}
                                                          key={items.hospitalId}
                                                          value={items.hospitalId}>
                                                    {hospitalList.find(hos=>hos.hospitalId==items.hospitalId).hospitalName}
                                                </MenuItem>
                                            )
                                        })
                                        }
                                    </TextField>

                                </div>
                                <div className="col-md-4 offset-1 row">
                                    <DateTimePicker name="appointmentDate" required label="Appointment Date"
                                                    InputProps={{
                                                        style: {
                                                            height: 44
                                                        }
                                                    }}
                                                    selected={values.appointmentDate}
                                                    onChange={date => setFieldValue('appointmentDate', date)}
                                    />
                                </div>
                            </div>
                            <div className="col-md-12 row">
                                {inputList.map((x, i) => {
                                    return (
                                        <div className="col-md-12 row">
                                            <div className="col-md-3  row">
                                                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                                    <KeyboardTimePicker
                                                        required
                                                        name="startTime"
                                                        value={x.startTime}
                                                        label="Start Time"
                                                        onChange={e => handleStartTime(e, i)}
                                                    />
                                                </MuiPickersUtilsProvider>
                                            </div>

                                            <div className="col-md-3 offset-2 row">
                                                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                                    <KeyboardTimePicker
                                                        required
                                                        name="endTime"
                                                        value={x.endTime}
                                                        label="End Time"
                                                        onChange={e => handleEndTime(e, i)}
                                                    />
                                                </MuiPickersUtilsProvider>
                                            </div>
                                            <div className="col-md-2 row">

                                                {
                                                    inputList.length !== 1 &&
                                                    <div className="col-md-2 row">
                                                        <IconButton onClick={() => handleRemove(i)} color="primary">
                                                            <DeleteIcon/>
                                                        </IconButton>
                                                    </div>


                                                }
                                                {
                                                    inputList.length - 1 === i &&
                                                    <div className="col-md-2 offset-1 row">
                                                        <IconButton onClick={handleAddMore} color="primary">
                                                            <AddBoxIcon/>
                                                        </IconButton>
                                                    </div>

                                                }

                                            </div>
                                        </div>
                                    )
                                })}

                            </div>
                            <div className="col-md-12 row">
                                <div className="col-md-1  row">
                                    <FormButton type="submit" buttonName="Submit">
                                    </FormButton>
                                    <Snackbar open={openSuccess} autoHideDuration={2000} onClose={handleCloseSuccess}
                                              anchorOrigin={{
                                                  vertical: 'bottom',
                                                  horizontal: 'right'
                                              }}>
                                        <Alert severity="success">Data added successfully.</Alert>
                                    </Snackbar>
                                </div>
                            </div>
                        </form>
                    );
                }}
            </Formik>
        </>
    )
}

export default HospitalScheduleTime;