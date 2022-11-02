import React, { useState, useMemo, useEffect } from "react";
import { useSelector } from "react-redux";
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import FormHelperText from '@material-ui/core/FormHelperText';
import { useNavigate } from 'react-router-dom';
import Typography from '@material-ui/core/Typography';
import ChevronLeftRoundedIcon from '@material-ui/icons/ChevronLeftRounded';
import LinkIcon from '@material-ui/icons/Link';
import AddRoundedIcon from '@material-ui/icons/AddRounded';
import DeleteOutlinedIcon from '@material-ui/icons/DeleteOutlined';
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


const EditSocialMediaLink = () => {
    const classes = useStyles();
    const navigate = useNavigate();
    const { user: currentUser } = useSelector((state) => state.auth);

    const [userInfo, setUserInfo] = useState([]);

    const [socialMediaLink1, setSocialMediaLink1] = useState('');
    const [socialMediaLink2, setSocialMediaLink2] = useState('');
    const [socialMediaLink3, setSocialMediaLink3] = useState('');

    const [errorMsg, setErrorMsg] = useState('');
    const [successfull, setSuccessfull] = useState(false);
    const [loading, setLoading] = useState(false);

    let userId = currentUser.userId;

    useEffect(() => {
        getProfileInfo();
    }, []);

    const getProfileInfo = () => {
        profileService.getProfileInfo(userId).then(
            response => {
                setUserInfo(response.data);
                setSocialMediaLink1(response.data.socialMediaLink1);
                setSocialMediaLink2(response.data.socialMediaLink2);
                setSocialMediaLink3(response.data.socialMediaLink3);
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
        const data = {
            userId, socialMediaLink1, socialMediaLink2, socialMediaLink3
        };
        profileService.changeSocialMediaLink(data).then(
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
                                        <LinkIcon style={{ fontSize: 60 }} />
                                        <Typography component="h1" variant="h6" color="inherit" noWrap>
                                            Edit social media links
                                        </Typography>
                                    </div>
                                </div>
                                {successfull === true ? (
                                    <div className='text-center alert alert-success text-success animated bounceIn'>
                                        <h5><CheckCircle /> Confirmation!</h5>
                                        <strong> Guardian information updated successfully.</strong>
                                    </div>
                                ) : (
                                    <form className={classes.root}>
                                        <div className="mb-2">You are required to add social media links such as facebook, linkedin, instagram etc. You can add upto 3 links.</div>

                                        <div className='col-md-10 row'>
                                            <TextField label="Social media link 1" required
                                                value={socialMediaLink1}
                                                onChange={(e) => { setSocialMediaLink1(e.target.value) }} />
                                        </div>
                                        <div className='col-md-10 row'>
                                            <TextField label="Social media link 2" required
                                                value={socialMediaLink2}
                                                onChange={(e) => { setSocialMediaLink2(e.target.value) }} />
                                        </div>
                                        <div className='col-md-10 row'>
                                            <TextField label="Social media link 3" required
                                                value={socialMediaLink3}
                                                onChange={(e) => { setSocialMediaLink3(e.target.value) }} />
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

export default EditSocialMediaLink;