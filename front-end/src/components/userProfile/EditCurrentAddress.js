import React, { useState, useMemo, useEffect } from "react";
import { useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import Typography from '@material-ui/core/Typography';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import LocationOnIcon from '@material-ui/icons/LocationOn';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import countryList from 'react-select-country-list';
import profileService from "../../services/profile.service";
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';


const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiFormControl-root': {
            width: '100%',
            margin: theme.spacing(1)
        }
    },
    formControl: {
        margin: theme.spacing(1),
        minWidth: '100%',
    },
    generalSetting: {
        width: '100%',
        maxWidth: 500,
    },
}));


const EditCurrentAddress = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);

    const [errorMsg, setErrorMsg] = useState('');
    const [successfull, setSuccessfull] = useState(false);
    const [loading, setLoading] = useState(false);

    const [showDzoAndGeog, setShowDzoAndGeog] = useState(true);
    const options = useMemo(() => countryList().getData(), []);
    const [presentCountry, setPresentCountry] = useState('');
    const [presentPlaceName, setPresentPlaceName] = useState('');
    const [allDzongkhags, setAllDzongkhags] = useState([]);
    const [allGeogs, setAllGeogs] = useState([]);
    const [presentDzongkhagId, setPresentDzongkhagId] = useState('');
    const [presentGeogId, setPresentGeogId] = useState('');

    let userId = currentUser.userId;

    useEffect(() => {
        setPresentCountry('Bhutan');//set default country to Bhutan because majority of the users will be in Bhutan
        getDzongkhagList();
    }, []);

    useEffect(() => {
        getGeogByDzongkhagId();
    }, [presentDzongkhagId]);

    const getProfileInfo = () => {
        profileService.getProfileInfo(userId).then(
            response => {
                setPresentCountry(response.data.presentCountry);
                setPresentPlaceName(response.data.presentPlaceName);
                // setPresentGeogId(response.data.presentGeog.geogId);
                setPresentGeogId(response.data.presentGeogId);
                // setPresentDzongkhagId(response.data.presentDzongkhag.dzongkhagId);
                setPresentDzongkhagId(response.data.presentDzongkhagId);
                if (response.data.presentCountry !== 'Bhutan') {
                    setShowDzoAndGeog(false);
                } else {
                    setShowDzoAndGeog(true);
                }
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };

    const handleCountryChange = (e) => {
        setPresentCountry(e.target.value);
        if (e.target.value === 'Bhutan') {
            setShowDzoAndGeog(true);
        } else {
            setShowDzoAndGeog(false);
        }
    };

    const handleDzongkhagChange = (e) => {
        setPresentDzongkhagId(e.target.value);
    };

    const getDzongkhagList = () => {
        setAllGeogs(['']);
        profileService.getAllDzongkhags().then(
            response => {
                setAllDzongkhags(response.data);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
        getProfileInfo();
    };

    const getGeogByDzongkhagId = () => {
        profileService.getGeogByDzongkhagId(presentDzongkhagId).then(
            response => {
                setAllGeogs(response.data);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };

    const handleSubmit = (e) => {
        setLoading(true);
        const data = { userId, presentCountry, presentPlaceName, presentDzongkhagId, presentGeogId };

        profileService.changeCurrentAddress(data).then(
            response => {
                //show success message
                setSuccessfull(true);
                setLoading(false);
            },
            error => {
                //show error message 
                setLoading(false);
                setSuccessfull(false);
                setErrorMsg(error.response.data.message);
            }
        );
    }
    return (
        <>
            <div className="col-md-12 row mb-2">
                <div className="col-md-4">
                    <Button variant="contained" color="primary" size="small" onClick={() => navigate("/gyalsupProfile")}>
                        <ChevronLeftRoundedIcon /> Go back </Button>
                </div>
            </div>
            <div className="col-md-12 row">
                <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                    <div className="col-md-10 text-muted">
                        <div className="cards">
                            <div className="card-body">
                                <div className="col-md-12 row mb-2">
                                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                        <LocationOnIcon style={{ fontSize: 60 }} />
                                        <Typography component="h1" variant="h6" color="inherit" noWrap>
                                            Change current address
                                        </Typography>
                                    </div>
                                </div>
                                {successfull === true ? (
                                    <div className='text-center alert alert-success text-success animated bounceIn'>
                                        <h5><CheckCircle /> Confirmation!</h5>
                                        <strong> Present address updated successfully.</strong>
                                    </div>
                                ) : (
                                    <form className={classes.root} >
                                        <div className='col-md-12 row'>
                                            <div className='col-md-6'>
                                                <FormControl className={classes.formControl}>
                                                    <InputLabel id="demo-simple-select-helper-label">Country</InputLabel>
                                                    <Select
                                                        labelId="demo-simple-select-helper-label"
                                                        id="demo-simple-select-helper"
                                                        value={presentCountry}
                                                        onChange={handleCountryChange}>
                                                        {options && options.map((val, idx) => {
                                                            return (
                                                                <MenuItem key={idx} value={val.label}><span>{val.label}</span></MenuItem>
                                                            );
                                                        })}

                                                    </Select>
                                                    <FormHelperText>Country where you currently live</FormHelperText>
                                                </FormControl>
                                            </div>
                                            <div className='col-md-6'>
                                                <FormControl className={classes.formControl}>
                                                    <TextField label="Place name" required
                                                        value={presentPlaceName}
                                                        onChange={(e) => { setPresentPlaceName(e.target.value) }} />
                                                    <FormHelperText>Name of a place where you currently live</FormHelperText>
                                                </FormControl>
                                            </div>
                                        </div>
                                        <div className='col-md-12 row' hidden={showDzoAndGeog !== true} >
                                            <div className='col-md-6'>
                                                <FormControl className={classes.formControl}>
                                                    <InputLabel id="demo-simple-select-helper-label">Dzongkhag</InputLabel>
                                                    <Select
                                                        labelId="demo-simple-select-helper-label"
                                                        id="demo-simple-select-helper"
                                                        value={presentDzongkhagId}
                                                        onChange={handleDzongkhagChange}
                                                    >
                                                        {allDzongkhags && allDzongkhags.map((items, idx) => {
                                                            return (
                                                                <MenuItem key={idx}
                                                                    value={items.dzongkhagId}>
                                                                    <span> {items.dzongkhagName}</span>
                                                                </MenuItem>
                                                            );
                                                        })}
                                                    </Select>
                                                    <FormHelperText>Current Dzongkhag</FormHelperText>
                                                </FormControl>
                                            </div>
                                            <div className='col-md-6'>
                                                <FormControl className={classes.formControl}>
                                                    <InputLabel id="demo-simple-select-helper-label">Geog</InputLabel>
                                                    <Select
                                                        labelId="demo-simple-select-helper-label"
                                                        id="demo-simple-select-helper"
                                                        value={presentGeogId}
                                                        onChange={(e) => { setPresentGeogId(e.target.value) }}
                                                    >
                                                        {allGeogs.map((items, idx) => {
                                                            return (
                                                                <MenuItem key={idx}
                                                                    value={items.geogId}>
                                                                    <span> {items.geogName}</span>
                                                                </MenuItem>
                                                            )
                                                        })
                                                        }
                                                    </Select>
                                                    <FormHelperText>Current Geog</FormHelperText>
                                                </FormControl>
                                            </div>
                                        </div>
                                        <div className='col-md-12 row'>
                                            <div className='d-flex flex-wrap flex-column align-items-center'>
                                                <Button variant="contained" color="primary" onClick={handleSubmit}
                                                    disabled={loading}>
                                                    {loading && (
                                                        <span className="spinner-border spinner-border-sm"></span>
                                                    )}  Update
                                                </Button>
                                            </div>
                                        </div>
                                    </form>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div >
        </>
    )
}

export default EditCurrentAddress;