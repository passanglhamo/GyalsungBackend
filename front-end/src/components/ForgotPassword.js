import React, { useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from 'react-router-dom';
import CheckButton from "react-validation/build/button";
import Alert from '@material-ui/lab/Alert';
import IconButton from '@material-ui/core/IconButton';
import Collapse from '@material-ui/core/Collapse';
import CloseIcon from '@material-ui/icons/Close';
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

const ForgotPassword = () => {
    let navigate = useNavigate();
    const classes = useStyles();
    const form = useRef();
    const checkBtn = useRef();
    const [successful, setSuccessful] = useState(false);
    const [loading, setLoading] = useState(false);
    const [showAlert, setShowAlert] = useState(false);
    const [email, setEmail] = useState(undefined);
    const [responseMsg, setResponseMsg] = useState(undefined);

    const handleSubmit = (e) => {
        e.preventDefault();
        setSuccessful(false);
        setLoading(true);
        let domainName = window.location.origin;
        const data = { email, domainName };
        AuthService.requestPasswordChange(data).then(
            response => {
                setSuccessful(true);
                setLoading(false);
                setResponseMsg(response.data.message);
                setShowAlert(true);
            },
            error => {
                setSuccessful(false);
                setLoading(false);
                setResponseMsg(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
                setShowAlert(true);
            }
        );
    };
    return (
        <div className='d-flex justify-content-center'>
            <div className="col-lg-6">
                <div className="card">
                    <div className="card-body">
                        <div className="row align-items-center">
                            <div className='text-center p-1'>
                                <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
                                    Forgot password?
                                </Typography>
                            </div>
                            <Form className={classes.root} onSubmit={handleSubmit} ref={form}>
                                <TextField
                                    label="Email"
                                    variant="filled"
                                    required
                                    value={email}
                                    onChange={e => setEmail(e.target.value)}
                                />

                                <div className='col-md-12'>
                                    <div className='d-flex justify-content-end'>
                                        <Button variant="contained" color="primary" onClick={e => { navigate('/signin') }}>
                                            <ArrowBack />  Back to login
                                        </Button>
                                        <Button type="submit" variant="contained" color="primary"  >
                                            {loading && (
                                                <span className="spinner-border spinner-border-sm"></span>
                                            )} Request new password
                                        </Button>
                                    </div>
                                </div>

                                {/* <div className='col-md-12 row'>
                                    {showAlert && (
                                        <div className={`alert alert-dismissible ${successful ? "alert-success" : "alert-danger"}`} role="alert">
                                            <div className="d-flex">
                                                <div>
                                                    {responseMsg}
                                                </div>
                                            </div>
                                            <a className="btn-close cursor-pointer" onClick={() => setShowAlert(false)}></a>
                                        </div>
                                    )}
                                    <CheckButton style={{ display: "none" }} ref={checkBtn} />
                                </div> */}
                            </Form>
                            <Collapse in={showAlert}>
                                <Alert
                                    severity={`${successful ? "success" : "error"}`}
                                    action={
                                        <IconButton
                                            aria-label="close"
                                            color="inherit"
                                            size="small"
                                            onClick={() => {
                                                setShowAlert(false);
                                            }}
                                        >
                                            <CloseIcon fontSize="inherit" />
                                        </IconButton>
                                    }
                                >
                                    {responseMsg}
                                </Alert>
                            </Collapse>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ForgotPassword;