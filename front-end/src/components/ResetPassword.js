import React, { useState, useEffect, useRef } from 'react';
import { useDispatch, useSelector } from "react-redux";
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import CheckButton from "react-validation/build/button";
import { makeStyles } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import { ArrowBack } from '@material-ui/icons';
import Typography from '@material-ui/core/Typography';
import Form from "react-validation/build/form";
import AuthService from "../services/auth.service";

const useStyles = makeStyles(theme => ({
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
    title: {
        flexGrow: 1,
    },
}));

const ResetPassword = () => {
    let navigate = useNavigate();
    const classes = useStyles();
    const form = useRef();
    const checkBtn = useRef();
    const dispatch = useDispatch();
    const { message } = useSelector(state => state.message);
    const [successful, setSuccessful] = useState(false);
    const [loading, setLoading] = useState(false);
    const [showAlert, setShowAlert] = useState(false);
    const [newPassword, setNetNewPassword] = useState(undefined);
    const [reTypeNewPassword, setReTypeNewPassword] = useState(undefined);
    const [passwordShown, setPasswordShown] = useState(false);

    const [email, setEmail] = useState(undefined);
    const [requestId, setRequestId] = useState(undefined);
    const [responseMsg, setResponseMsg] = useState(undefined);
    const [content, setContent] = useState('');
    const location = useLocation();
    const params = new URLSearchParams(location.search);

    useEffect(() => {
        validatePasswordResetLink();
    }, []);

    const showPassword = () => {
        setPasswordShown(!passwordShown);
    }

    const validatePasswordResetLink = () => {
        const requestIdFromUrl = params.get("requestId");
        const emailFromUrl = params.get("email");
        AuthService.validatePasswordResetLink(requestIdFromUrl, emailFromUrl).then(
            response => {
                setEmail(response.data.email);
                setRequestId(response.data.requestId);
                if (response.data.status === 'C') {
                    setContent('You have already changed using this link. Please request new link.')
                }
            },
            error => {
                setContent(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        setSuccessful(false);
        setLoading(true);
        setShowAlert(false);
        if (newPassword !== reTypeNewPassword) {
            setShowAlert(true);
            setLoading(false);
            setResponseMsg("Password doesn't match.");
            return;
        }

        if (!showAlert) {
            AuthService.resetPassword(newPassword, email, requestId).then(
                response => {
                    setSuccessful(true);
                    setLoading(false);
                    setShowAlert(true);
                    setResponseMsg(response.data.message);
                    setNetNewPassword(undefined);
                    setReTypeNewPassword(undefined);
                },
                error => {
                    setShowAlert(true);
                    setSuccessful(false);
                    setLoading(false);
                    setResponseMsg((error.response && error.response.data && error.response.data.message) ||
                        error.message || error.toString());
                }
            );
        }
    };

    return (
        <div className='d-flex justify-content-center'>
            {content ? (<div className="">
                <div className="alert alert-danger">
                    <div className="row align-items-center">
                        <Typography>
                            {content}
                        </Typography>
                    </div>
                </div>
            </div>) : (
                <div className="col-lg-6">
                    <div className="card">
                        <div className="card-body">
                            <div className="row align-items-center">
                                <div className='text-center p-1'>
                                    <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
                                        Reset Password
                                    </Typography>
                                </div>
                                <div className='dropdown-divider'></div>
                                <Form className={classes.root} onSubmit={handleSubmit} ref={form}>
                                    <TextField
                                        label="New password"
                                        variant="filled"
                                        required
                                        type={passwordShown ? "text" : "password"}
                                        value={newPassword}
                                        onChange={e => setNetNewPassword(e.target.value)}
                                    />
                                    <TextField
                                        label="Re-type new password"
                                        variant="filled"
                                        required
                                        type={passwordShown ? "text" : "password"}
                                        value={reTypeNewPassword}
                                        onChange={e => setReTypeNewPassword(e.target.value)}
                                    />
                                    <div className='col-md-12'>
                                        <div className='d-flex justify-content-begin'>
                                            <small className='pr-3 cursor-pointer' onClick={showPassword}>
                                                <input type="checkbox" id="showPw" /> <label for="showPw" className="cursor-pointer" onClick={showPassword}>Show password</label>
                                            </small>
                                        </div>
                                    </div>

                                    <div className='col-md-12'>
                                        <div className='d-flex justify-content-end'>
                                            <Button variant="contained" color="primary" onClick={e => { navigate('/signin') }}>
                                                <ArrowBack />  Back to login
                                            </Button>
                                            <Button type="submit" variant="contained" color="primary"  >
                                                {loading && (
                                                    <span className="spinner-border spinner-border-sm"></span>
                                                )} Change Now
                                            </Button>
                                        </div>
                                    </div>
                                    <div className='col-md-12 row'>
                                        {showAlert && (
                                            <div className={`alert alert-dismissible ${successful ? "alert-success" : "alert-danger"}`} role="alert">
                                                <div className="d-flex">
                                                    <div>
                                                        {message}{responseMsg}
                                                    </div>
                                                </div>
                                                <a className="btn-close cursor-pointer" onClick={() => setShowAlert(false)}></a>
                                            </div>
                                        )}
                                        <CheckButton style={{ display: "none" }} ref={checkBtn} />
                                    </div>
                                </Form>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}

export default ResetPassword;