import React, { useState, useMemo, useEffect } from "react";
import { useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import Typography from '@material-ui/core/Typography';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import profileService from "../../services/profile.service";
import { ArrowBack, ArrowForward, CheckCircle } from '@material-ui/icons';
import LockOpenOutlinedIcon from '@material-ui/icons/LockOpenOutlined';

const useStyles = makeStyles((theme) => ({
    root: {
        padding: theme.spacing(0, 5),
        '& .MuiTextField-root': {
            width: '100%',
            borderRadius: '25px',
        },
    },
}));


const EditUsername = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);
    const [username, setUsername] = useState('');
    const [errorMsg, setErrorMsg] = useState('');
    const [showErrorMsg, setShowErrorMsg] = useState(false);
    const [successfull, setSuccessfull] = useState(false);
    const [loading, setLoading] = useState(false);
    const [userInfo, setUserInfo] = useState([]);
    const [showTips, setThowTips] = useState(false)

    let userId = currentUser.userId;

    useEffect(() => {
        getProfileInfo();
    }, []);

    const getProfileInfo = () => {
        profileService.getProfileInfo(userId).then(
            response => {
                setUserInfo(response.data);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };

    const handleChangeUsername = (e) => {
        setUsername(e.target.value);
    };

    const toggleUsernameTips = () => {
        setThowTips(!showTips);
    };

    const handleSubmit = (e) => {
        if (username === '') {
            setShowErrorMsg(true);
            setErrorMsg('Username is required.');
        } else {
            setLoading(true);
            const data = { userId, username };
            profileService.changeUsername(data).then(
                response => {
                    //show success message
                    setSuccessfull(true);
                    setLoading(false);
                    setShowErrorMsg(false);
                },
                error => {
                    //show error message 
                    setLoading(false);
                    setSuccessfull(false);
                    setShowErrorMsg(true);
                    setErrorMsg(error.response.data.message);
                }
            );
        }
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
                    <div className="col-md-8 text-muted">
                        <div className="cards">
                            <div className="card-body">
                                <div className="col-md-12 row mb-2">
                                    <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                                        <LockOpenOutlinedIcon style={{ fontSize: 60 }} />
                                        <Typography component="h1" variant="h6" color="inherit" noWrap>
                                            Set username
                                        </Typography>
                                    </div>
                                </div>
                                {successfull === true ? (
                                    <div className='text-center alert alert-success text-success animated bounceIn'>
                                        <h5><CheckCircle /> Confirmation!</h5>
                                        <strong> Your username has been set successfully.</strong>
                                    </div>
                                ) : (
                                    <div className={classes.root}>
                                        <div className='col-md-12 row mb-2'>
                                            <span>
                                                Unique username is required to login. System will check the availability of
                                                username.
                                            </span>
                                        </div>
                                        <div className='col-md-12'>
                                            Your current username is: {userInfo.username}
                                        </div>
                                        <div className='mb-2'>
                                            <TextField
                                                label="Username"
                                                required
                                                error={showErrorMsg}
                                                helperText={showErrorMsg === true ? errorMsg : ''}
                                                value={username}
                                                onChange={handleChangeUsername}
                                            />
                                        </div>
                                        {/* <div className='mb-2'> */}
                                        <div className="d-flex flex-wrap justify-content-between mb-4">
                                            <Button variant="contained" color="primary" onClick={handleSubmit} disabled={loading}>
                                                {loading && (<span className="spinner-border spinner-border-sm"></span>)}
                                                Update
                                            </Button>
                                            <span>Need help?
                                                <small className="cursor-pointer infoMsg" onClick={toggleUsernameTips}> Get tips on chosing a username</small>
                                            </span>
                                        </div>
                                        <div hidden={!showTips} className="mb-4 animated fadeIn">
                                            <strong>Guidelines for creating a username</strong>
                                            <div className="mb-2">
                                                When you create a username for your profile, keep in mind:
                                            </div>
                                            <div className="mb-2">
                                                <ul>
                                                    <li>
                                                        You can only have one username for your profile and you can't have
                                                        a username that is already being used.
                                                    </li>
                                                    <li>
                                                        Username can only contain aphanumeric characters (A-Z, 0-9) and periods(".").
                                                        They cabt't contain generic terms or extensions (.com, .net etc).
                                                    </li>
                                                    <li>
                                                        Usernames must be at least 5 characters long.
                                                    </li>
                                                    <li>
                                                        Usernames shouldn't impersonate someone else.
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div >
        </>
    )
}

export default EditUsername;