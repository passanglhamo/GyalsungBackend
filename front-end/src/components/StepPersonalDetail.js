
import 'date-fns';
import React, { useState } from 'react';
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import moment from 'moment';
import Grid from '@material-ui/core/Grid';
import DateFnsUtils from '@date-io/date-fns';
import { MuiPickersUtilsProvider, KeyboardDatePicker, } from '@material-ui/pickers';
import registerService from "../services/register.service";

const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiTextField-root': {
            margin: theme.spacing(1),
            width: '100%',
        },
    },
}));

const StepPersonalDetail = () => {

    const classes = useStyles();

    const [cid, setCid] = useState(undefined);
    const [dob, setDob] = useState(undefined);
    const [cidNumber, setCidNumber] = useState(undefined);
    const [dateOfBirth, setDateOfBirth] = useState(null);//when set to null default is empty in datepicker
    const [fullName, setFullName] = useState(undefined);
    const [gender, setGender] = useState(undefined);
    const [fatherName, setFatherName] = useState(undefined);
    const [motherName, setMotherName] = useState(undefined);
    const [villageName, setVillageName] = useState(undefined);
    const [geogName, setGeogName] = useState(undefined);
    const [dzongkhagName, setDzongkhagName] = useState(undefined);
    const [houseNo, setHouseNo] = useState(undefined);
    const [thramNo, setThramNo] = useState(undefined);
    const [successful, setSuccessful] = useState(false);
    const [loading, setLoading] = useState(false);
    const [errorMsg, setErrorMsg] = useState(undefined);
    const [showAlert, setShowAlert] = useState(false);


    const handleDobChange = (date) => {
        setDateOfBirth(date);
    };

    const getCitizenDetail = () => {
        setLoading(true);
        registerService.getCitizenDetail(cidNumber, moment(dateOfBirth).format('DD/MM/YYYY')).then(
            response => {
                setFullName(response.data.fullName);
                setGender(response.data.gender);
                setFatherName(response.data.fatherName);
                setMotherName(response.data.motherName);
                setHouseNo(response.data.houseNo);
                setThramNo(response.data.thramNo);
                setVillageName(response.data.villageName);
                setGeogName(response.data.geogName);
                setDzongkhagName(response.data.dzongkhagName);
                setCid(response.data.cid);
                setDob(response.data.dob);
                setCid(response.data.cid);
                setDob(response.data.dob);
                setLoading(false);
                setSuccessful(true);
            },
            error => {
                setLoading(false);
                setSuccessful(false);
                // alert(error)
                setErrorMsg(error.response.data.message);
                setShowAlert(true);
                //set all personal detail(fullName, cid, gender, dob, etc.) to undefined
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    }

    return {
        cid, dob, fullName, gender, fatherName, motherName, villageName,
        geogName, dzongkhagName, houseNo, thramNo, personalDetailStep: (
            <div className='card'>
                <div className='card-body'>
                    <div className='col-md-12 row'>
                        <Grid container justifyContent="space-around">
                            <div className='col-md-4 offset-1'>
                                <TextField
                                    id="standard-textarea"
                                    label="CID"
                                    required
                                    value={cidNumber}
                                    onChange={e => { setCidNumber(e.target.value) }}
                                />
                            </div>

                            <div className='col-md-2'>
                                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                    <KeyboardDatePicker
                                        margin="normal"
                                        id="date-picker-dialog"
                                        label="DOB"
                                        required
                                        format="dd/MM/yyyy"
                                        placeholder="dd/MM/yyyy"
                                        value={dateOfBirth}
                                        onChange={handleDobChange}
                                        KeyboardButtonProps={{
                                            'aria-label': 'change date',
                                        }}
                                    />
                                </MuiPickersUtilsProvider>
                            </div>
                            <div className='col-md-2'>
                                <Button variant="contained" color="primary"
                                    className={classes.backButton}
                                    onClick={() => { getCitizenDetail(); }}
                                    disabled={loading}
                                >  {loading && (
                                    <span className="spinner-border spinner-border-sm"></span>
                                )} Check my detail </Button>
                            </div>
                        </Grid>
                    </div>
                    {showAlert && (
                        <div className="alert alert-danger alert-dismissible" role="alert">
                            <div className="d-flex">
                                <div>
                                    {errorMsg}
                                </div>
                            </div>
                            <a className="btn-close cursor-pointer" onClick={() => setShowAlert(false)}></a>
                        </div>
                    )}
                    <div className='mb-4'></div>
                    <div className='dropdown-divider'></div>

                    <div className="col-md-12 row">
                        <div className="col-md-4">
                            <div className={`card ${successful ? "bg-light-green" : ""}`}>
                                <div className="card-body">
                                    <div className="mb-2">
                                        Full name: <strong>{fullName}</strong>
                                    </div>
                                    <div className="mb-2">
                                        Gender: <strong>{gender}</strong>
                                    </div>
                                    <div className="mb-2">
                                        CID: <strong>{cid}</strong>
                                    </div>

                                    <div className="mb-2">
                                        Birth date: <strong>{dob}</strong>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-md-4">
                            <div className={`card ${successful ? "bg-light-green" : ""}`}>
                                <div className="card-body">
                                    <div className="mb-2">
                                        Father's name: <strong>{fatherName}</strong>
                                    </div>
                                    <div className="mb-2">
                                        Mother's name: <strong>{motherName}</strong>
                                    </div>
                                    <div className="mb-2">
                                        House number: <strong>{houseNo}</strong>
                                    </div>

                                    <div className="mb-2">
                                        Thram number: <strong>{thramNo}</strong>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-md-4">
                            <div className={`card ${successful ? "bg-light-green" : ""}`}>
                                <div className="card-body">
                                    <div className="mb-2">
                                        Village: <strong>{villageName}</strong>
                                    </div>
                                    <div className="mb-2">
                                        Geog: <strong>{geogName}</strong>
                                    </div>
                                    <div className="mb-2">
                                        Dzongkhag: <strong>{dzongkhagName}</strong>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default StepPersonalDetail;