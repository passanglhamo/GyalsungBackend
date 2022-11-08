import React, {useEffect, useState} from "react";
import {useSelector} from "react-redux";

import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faBirthdayCake,
    faBriefcase,
    faClock,
    faEnvelope,
    faFemale,
    faHandsHelping,
    faHome,
    faIdCard,
    faLink,
    faLocationDot,
    faMale,
    faPhone,
    faUnlock
} from "@fortawesome/free-solid-svg-icons";
import {makeStyles, withStyles} from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import Avatar from '@material-ui/core/Avatar';
import MailOutlineIcon from '@material-ui/icons/MailOutline';
import PhoneAndroidIcon from '@material-ui/icons/PhoneAndroid';
import LocationOnIcon from '@material-ui/icons/LocationOn';
import GroupIcon from '@material-ui/icons/Group';
import PersonIcon from '@material-ui/icons/Person';
import LinkIcon from '@material-ui/icons/Link';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import ChevronRightRoundedIcon from '@material-ui/icons/ChevronRightRounded';
import LockOpenOutlinedIcon from '@material-ui/icons/LockOpenOutlined';
import EditLocationOutlinedIcon from '@material-ui/icons/EditLocationOutlined';
import VpnKeyIcon from '@material-ui/icons/VpnKey';
import moment from 'moment';
import {useNavigate} from 'react-router-dom';
import profileService from "../../services/profile.service";
import {ImageViewer} from "react-image-viewer-dv";
import fileDownload from "js-file-download";

const AntTabs = withStyles({
    root: {
        borderBottom: '1px solid #e8e8e8',
    },
    indicator: {
        backgroundColor: '#1890ff',
    },
})(Tabs);

const AntTab = withStyles((theme) => ({
    root: {
        textTransform: 'none',
        minWidth: 72,
        fontWeight: theme.typography.fontWeightRegular,
        marginRight: theme.spacing(4),
        '&:hover': {
            color: '#40a9ff',
            opacity: 1,
        },
        '&$selected': {
            color: '#1890ff',
            fontWeight: theme.typography.fontWeightMedium,
        },
        '&:focus': {
            color: '#40a9ff',
        },
    },
    selected: {},
}))((props) => <Tab  {...props} />);


const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiFormControl-root': {
            width: '80%',
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


const GyalsupProfile = () => {
    const classes = useStyles();
    const {user: currentUser} = useSelector((state) => state.auth);

    const [userInfo, setUserInfo] = useState([]);
    // const [presentDzongkhagName, setPresentDzongkhagName] = useState('');
    // const [presentGeogName, setPresentGeogName] = useState('');
    const [presentCountry, setPresentCountry] = useState('');
    const [isProfileNull, setIsProfileNull] = useState(false);
    const [profilePhoto, setProfilePhoto] = useState([]);


    const [tabValue, setTabValue] = React.useState(0);
    const navigate = useNavigate();
    let userId = currentUser.userId;

    useEffect(() => {
        getProfilePicture();
        getProfileInfo();
        handleProfilePictureChange();
    }, []);


    const downloadFile = async () => {
        let url = userInfo.profilePictureUrl;
        let profilePictureName = userInfo.profilePictureName;
        const response = await profileService.downloadFile(url);
        fileDownload(response.data, profilePictureName);
    };

    const getProfilePicture = () => {
        profileService.getProfilePicture(userId).then(
            response => {
                setProfilePhoto(response.data.profilePhoto);
                setIsProfileNull(false);
            },
            error => {
                setIsProfileNull(true);
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };

    const getProfileInfo = () => {
        profileService.getProfileInfo(userId).then(
            response => {
                setUserInfo(response.data);
                // setPresentDzongkhagName(response.data.presentDzongkhag.dzongkhagName);
                // setPresentGeogName(response.data.presentGeog.geogName);
                setPresentCountry(response.data.presentCountry);
                // setProfilePicName("/profilePic/" + response.data.profilePictureName);
            },
            error => {
                console.log(
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message || error.toString()
                );
            }
        );
    };

    let presentAddress = userInfo.presentPlaceName + ", " + userInfo.presentGeogName + ", " + userInfo.presentDzongkhagName + " " + presentCountry;
    let permanantAddress = userInfo.permanentPlaceName + ", " + userInfo.permanentGeog + ", " + userInfo.permanentDzongkhag;
    if (presentCountry !== 'Bhutan') {
        presentAddress = userInfo.presentPlaceName + ", " + presentCountry;
    }

    const toggleTab = (event, newValue) => {
        setTabValue(newValue);
    };

    const handleProfilePictureChange = () => {
        const imgDiv = document.querySelector('.profile-pic-div');
        const img = document.querySelector('#profilePhoto');
        const file = document.querySelector('#profileImg');
        const uploadBtn = document.querySelector('#uploadProfilePicBtn');

        //if user hover on img div
        imgDiv.addEventListener('mouseenter', function () {
            uploadBtn.style.display = "block";
        });
        //if we hover out from img div
        imgDiv.addEventListener('mouseleave', function () {
            uploadBtn.style.display = "none";
        });

        //when we choose a foto to upload
        file.addEventListener('change', function () {
            //this refers to file
            const profilePicture = this.files[0];
            if (profilePicture) {
                const data = {userId, profilePicture};
                profileService.changeProfilePic(data).then(
                    response => {
                        // const reader = new FileReader(); //FileReader is a predefined function of JS
                        // reader.addEventListener('load', function () {
                        //     img.setAttribute('src', reader.result);
                        // });
                        // reader.readAsDataURL(profilePicture);
                        getProfilePicture();
                        window.location.reload();
                    },
                    error => {

                    }
                );
            }
        });
    }

    return (
        <div className="col-md-12 row">
            <div className="col-md-4">
                <div className="card">
                    <div className="card-body">
                        <div className="d-flex flex-wrap flex-column align-items-center justify-content-center">
                            <div className="profile-pic-div">
                                {isProfileNull == true ?
                                    <img src="//ssl.gstatic.com/accounts/ui/avatar_2x.png" id="profilePhoto" alt="IMG"/>
                                    : <div>
                                        <ImageViewer>
                                            <img src={`data:image/jpeg;base64,${profilePhoto}`} id="profilePhoto"
                                                 alt="IMG"/>
                                        </ImageViewer>
                                    </div>
                                }
                                <input type="file" id="profileImg" accept="image/png, image/jpeg"/>
                                <div id="uploadProfilePicBtn">
                                    <label htmlFor="profileImg">Change Profile</label>
                                </div>
                            </div>

                            {/* <div className="mb-2">
                                <Button size="small" variant="contained" color="primary" onClick={downloadFile}>
                                    Download Profile
                                </Button>
                            </div> */}

                            <div className="mb-2">
                                <h5 className="text-muted">
                                    {/* <VerifiedUserIcon className="text-success" title="You are verified as a Gyalsuup" /> */}
                                    &nbsp;{userInfo.fullName}
                                </h5>
                            </div>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3"><FontAwesomeIcon
                                icon={faLocationDot}/> Lives in </span> <strong
                            className="text-muted">{presentAddress}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3"><FontAwesomeIcon
                                icon={faPhone}/></span> <strong className="text-muted">{userInfo.mobileNo}</strong>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3"><FontAwesomeIcon icon={faEnvelope}/></span>
                            <strong className="text-muted">{userInfo.email}</strong>
                        </div>
                        <div className="dropdown-divider"></div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3"><FontAwesomeIcon icon={faIdCard}/> CID </span>
                            <strong className="text-muted">{userInfo.cid}</strong><br></br>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3"><FontAwesomeIcon icon={faUnlock}/> Username </span>
                            <strong className="text-muted">{userInfo.username}</strong><br></br>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3"><FontAwesomeIcon icon={faMale}/> Gender </span>
                            <strong className="text-muted">{userInfo.sex}</strong><br></br>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3"><FontAwesomeIcon
                                icon={faBirthdayCake}/> Born on </span>
                            <strong className="text-muted">
                                {moment(userInfo.dob).format('MMM MM, YYYY')}</strong><br></br>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3"><FontAwesomeIcon icon={faHome}/> From </span>
                            <strong className="text-muted">{permanantAddress}</strong><br></br>
                        </div>
                        <div className="mb-2">
                            <span className="text-muted text-sm-right mb-0 mb-sm-3"><FontAwesomeIcon icon={faClock}/> Joined on </span>
                            <strong className="text-muted">{moment(userInfo.createdDate).format('MMM MM, YYYY')}
                            </strong><br></br>
                        </div>
                        <hr></hr>
                        <div className="d-flex justify-content-between align-items-center">
                            <strong className="text-muted text-sm-left mb-0 mb-sm-3"><FontAwesomeIcon
                                icon={faLink}/> Social media </strong>
                        </div>
                        <div className="mb-2">
                            <a className="text-muted text-sm-right mb-0 mb-sm-3"
                               href={userInfo.socialMediaLink1}
                               target="_blank">
                                {userInfo.socialMediaLink1}
                            </a>
                        </div>
                        <div className="mb-2">
                            <a className="text-muted text-sm-right mb-0 mb-sm-3"
                               href={userInfo.socialMediaLink2}
                               target="_blank">
                                {userInfo.socialMediaLink2}
                            </a>
                        </div>
                        <div className="mb-2">
                            <a className="text-muted text-sm-right mb-0 mb-sm-3"
                               href={userInfo.socialMediaLink3}
                               target="_blank">
                                {userInfo.socialMediaLink3}
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <div className="col-md-8">
                <div className="card">
                    <div className="card-body">
                        <div>
                            <AntTabs value={tabValue} onChange={toggleTab} aria-label="Tabs">
                                <AntTab label="General information"/>
                                <AntTab label="General setting"/>
                                <AntTab label="Security setting"/>
                            </AntTabs>
                            <div className='mb-2'></div>
                            <div hidden={tabValue !== 0} className="general-info-tab">
                                <div className="d-flex justify-content-between align-items-center">
                                    <strong className="text-muted text-sm-left mb-0 mb-sm-3">
                                        Contact information
                                    </strong>
                                </div>
                                <div className="dropdown-divider"></div>
                                <div className="mb-2">
                                    <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                        <FontAwesomeIcon icon={faPhone}/></span> Phone: <strong
                                    className="text-muted">{userInfo.mobileNo}</strong>
                                </div>
                                <div className="mb-2">
                                    <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                        <FontAwesomeIcon icon={faEnvelope}/></span> Email: <strong
                                    className="text-muted">{userInfo.email}</strong>
                                </div>
                                <div className="mb-2">
                                    <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                        <FontAwesomeIcon icon={faLocationDot}/></span>
                                    Address: <strong className="text-muted">{presentAddress}
                                </strong>
                                </div>
                                <hr></hr>
                                <div className="d-flex justify-content-between align-items-center">
                                    <strong className="text-muted text-sm-left mb-0 mb-sm-3">
                                        Parent information
                                    </strong>
                                </div>
                                <div className="dropdown-divider"></div>
                                <div className="col-md-12 row">
                                    <div className="col-md-6">
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faMale}/></span> Father's Name:
                                            <strong className="text-muted"> {userInfo.fatherName}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faIdCard}/></span> Father's CID:
                                            <strong className="text-muted"> {userInfo.fatherCid}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faBriefcase}/></span> Father's Occupation:
                                            <strong className="text-muted"> {userInfo.fatherOccupation}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faPhone}/></span> Father's Phone:
                                            <strong className="text-muted"> {userInfo.fatherMobileNo}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faEnvelope}/></span> Father's Email:
                                            <strong className="text-muted"> {userInfo.fatherEmail}</strong>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faFemale}/></span> Mother's Name:
                                            <strong className="text-muted"> {userInfo.motherName}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faIdCard}/></span> Mother's CID:
                                            <strong className="text-muted"> {userInfo.motherCid}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faBriefcase}/></span> Mother's Occupation:
                                            <strong className="text-muted"> {userInfo.motherOccupation}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faPhone}/></span> Mother's Phone:
                                            <strong className="text-muted"> {userInfo.motherMobileNo}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faEnvelope}/></span> Mother's Email:
                                            <strong className="text-muted"> {userInfo.motherEmail}</strong>
                                        </div>
                                    </div>
                                </div>
                                <hr></hr>
                                <div className="col-md-12 row">
                                    <div className="d-flex justify-content-between align-items-center">
                                        <strong className="text-muted text-sm-left mb-0 mb-sm-3">
                                            Guardian information
                                        </strong>
                                    </div>
                                    <div className="col-7">
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faMale}/></span> Guardian's Name:
                                            <strong className="text-muted"> {userInfo.guardianName}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faIdCard}/></span> Guardian's CID:
                                            <strong className="text-muted"> {userInfo.guardianCid}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faBriefcase}/></span> Guardian's Occupation:
                                            <strong className="text-muted"> {userInfo.guardianOccupation}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faPhone}/></span> Guardian's Phone:
                                            <strong className="text-muted"> {userInfo.guardianMobileNo}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faEnvelope}/></span> Guardian's Email:
                                            <strong className="text-muted"> {userInfo.guardianEmail}</strong>
                                        </div>
                                        <div className="mb-2">
                                            <span className="text-muted text-sm-right mb-0 mb-sm-3">
                                                <FontAwesomeIcon icon={faHandsHelping}/></span> Relation to Guardian:
                                            <strong className="text-muted"> {userInfo.relationToGuardian}</strong>
                                        </div>
                                    </div>
                                </div>

                                {/* <div className="dropdown-divider"></div>
                        <div className="d-flex justify-content-between align-items-center">
                            <strong className="text-muted text-sm-left mb-0 mb-sm-3"> Education </strong>
                        </div>
                        <div className="dropdown-divider"></div>
                        <div className="mb-2">
                            <FontAwesomeIcon icon={faBook} /> Completed XII Science from Tashitse HSS in 2022
                        </div> */}
                            </div>

                            <div hidden={tabValue !== 1} className="general-setting-tab">
                                <List className={classes.generalSetting}>
                                    <ListItem button onClick={() => navigate("/editCurrentAddress")}>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <LocationOnIcon/>
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary="Edit current address"
                                                      secondary="Country and place where you currently live"/>
                                        <ChevronRightRoundedIcon/>
                                    </ListItem>
                                    <ListItem button onClick={() => navigate("/editParentInfo")}>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <GroupIcon/>
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary="Edit parent information"
                                                      secondary="Parent's contact detail and occupation"/>
                                        <ChevronRightRoundedIcon/>
                                    </ListItem>
                                    <ListItem button onClick={() => navigate("/editGuardianInfo")}>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <PersonIcon/>
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary="Edit guardian information"
                                                      secondary="Guardian's contact detail, occupation and your relation to guardian"/>
                                        <ChevronRightRoundedIcon/>
                                    </ListItem>
                                    <ListItem button onClick={() => navigate("/editSocialMediaLink")}>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <LinkIcon/>
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary="Edit social media links"
                                                      secondary="Facebook, Linked etc"/>
                                        <ChevronRightRoundedIcon/>
                                    </ListItem>
                                    <ListItem button onClick={() => navigate("/editSynceCensusRecord")}>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <EditLocationOutlinedIcon/>
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary="Update census records"
                                                      secondary="If your CID, name and permanant address are changed in national census record"/>
                                        <ChevronRightRoundedIcon/>
                                    </ListItem>
                                </List>
                            </div>
                            <div hidden={tabValue !== 2} className="change-password-tab">
                                <List className={classes.generalSetting}>
                                    <ListItem button onClick={() => navigate("/editMobileNo")}>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <PhoneAndroidIcon/>
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary="Change mobile number"
                                                      secondary="Required correct mobile number"/>
                                        <ChevronRightRoundedIcon/>
                                    </ListItem>
                                    <ListItem button onClick={() => navigate("/editEmailAddress")}>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <MailOutlineIcon/>
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary="Change email address"
                                                      secondary="Required valid email address"/>
                                        <ChevronRightRoundedIcon/>
                                    </ListItem>
                                    <ListItem button onClick={() => navigate("/editUsername")}>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <LockOpenOutlinedIcon/>
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary="Set username"
                                                      secondary="Unique username is required to login"/>
                                        <ChevronRightRoundedIcon/>
                                    </ListItem>
                                    <ListItem button onClick={() => navigate("/editPasswordChange")}>
                                        <ListItemAvatar>
                                            <Avatar>
                                                <VpnKeyIcon/>
                                            </Avatar>
                                        </ListItemAvatar>
                                        <ListItemText primary="Change password"
                                                      secondary="Recommend strong and secured password"/>
                                        <ChevronRightRoundedIcon/>
                                    </ListItem>
                                </List>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default GyalsupProfile;