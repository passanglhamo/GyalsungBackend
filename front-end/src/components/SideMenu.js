import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import DashboardIcon from '@material-ui/icons/Dashboard';
import PeopleIcon from '@material-ui/icons/People';
import RepeatIcon from '@material-ui/icons/Repeat';
import GroupAddIcon from '@material-ui/icons/GroupAdd';
import LocalHospitalIcon from '@material-ui/icons/LocalHospital';
import PersonIcon from '@material-ui/icons/Person';
import AssignmentIcon from '@material-ui/icons/Assignment';
import Divider from '@material-ui/core/Divider';
import PanToolIcon from '@material-ui/icons/PanTool';
import AutorenewIcon from '@material-ui/icons/Autorenew';
import StarsIcon from '@material-ui/icons/Stars';
import VisibilityOffIcon from '@material-ui/icons/VisibilityOff';
import PlaylistAddCheckIcon from '@material-ui/icons/PlaylistAddCheck';
import { useNavigate } from 'react-router-dom';


const SideMenu = () => {
    const navigate = useNavigate();

    return (
        <div>
            <ListSubheader inset>Admin Menu</ListSubheader>
            <ListItem button onClick={() => navigate("/")} >
                <ListItemIcon>
                    <DashboardIcon />
                </ListItemIcon>
                <ListItemText primary="Dashboard" />
            </ListItem>
            {/* <ListItem button onClick={() => navigate("/registeredUser")} >
                <ListItemIcon>
                    <GroupAddIcon />
                </ListItemIcon>
                <ListItemText primary="Registered Users" />
            </ListItem> */}
            <ListItem button onClick={() => navigate("/submitParentConsent")} >
                <ListItemIcon>
                    <PlaylistAddCheckIcon />
                </ListItemIcon>
                <ListItemText primary="Submit Parent Consent" />
            </ListItem>
            <ListItem button onClick={() => navigate("/parentConsentList")} >
                <ListItemIcon>
                    <PlaylistAddCheckIcon />
                </ListItemIcon>
                <ListItemText primary="Parent Consent List" />
            </ListItem>
            <ListItem button onClick={() => navigate("/enrolment")} >
                <ListItemIcon>
                    <PlaylistAddCheckIcon />
                </ListItemIcon>
                <ListItemText primary="Enrolment" />
            </ListItem>
            <ListItem button onClick={() => navigate("/enrolmentList")} >
                <ListItemIcon>
                    <PlaylistAddCheckIcon />
                </ListItemIcon>
                <ListItemText primary="Enrolment List" />
            </ListItem>
            <ListItem button onClick={() => navigate("/applyDeferment")}>
                <ListItemIcon>
                    <RepeatIcon />
                </ListItemIcon>
                <ListItemText primary="Apply Deferment" />
            </ListItem>
            <ListItem button onClick={() => navigate("/defermentList")}>
                <ListItemIcon>
                    <RepeatIcon />
                </ListItemIcon>
                <ListItemText primary="Deferment List" />
            </ListItem>
            <ListItem button onClick={() => navigate("/applyExemption")}>
                <ListItemIcon>
                    <PanToolIcon />
                </ListItemIcon>
                <ListItemText primary="Apply Exemption" />
            </ListItem>
            <ListItem button onClick={() => navigate("/exemptionList")}>
                <ListItemIcon>
                    <RepeatIcon />
                </ListItemIcon>
                <ListItemText primary="Exemption List" />
            </ListItem>
            <ListItem button onClick={() => navigate("/medicalBooking")}>
                <ListItemIcon>
                    <LocalHospitalIcon />
                </ListItemIcon>
                <ListItemText primary="Medical Booking" />
            </ListItem>
            <ListItem button onClick={() => navigate("/changeMedicalAppointment")}>
                <ListItemIcon>
                    <LocalHospitalIcon />
                </ListItemIcon>
                <ListItemText primary="Edit Medical Booking" />
            </ListItem>

            <ListItem button onClick={() => navigate("/trainingAcademyDashboard")}>
                <ListItemIcon>
                    <PlaylistAddCheckIcon />
                </ListItemIcon>
                <ListItemText primary="Training Academy" />
            </ListItem>

            <ListItem button onClick={() => navigate("/academyWiseEnrolment")}>
                <ListItemIcon>
                    <PlaylistAddCheckIcon />
                </ListItemIcon>
                <ListItemText primary="Academy wise enrolment" />
            </ListItem>
            <Divider />
            <ListSubheader inset> Master Data</ListSubheader>
            {/* <ListItem button onClick={() => navigate("/userList")}>
                <ListItemIcon>
                    <GroupAddIcon />
                </ListItemIcon>
                <ListItemText primary="User List" />
            </ListItem>
            <ListItem button onClick={() => navigate("/roleCreation")}>
                <ListItemIcon>
                    <StarsIcon />
                </ListItemIcon>
                <ListItemText primary="Role Creation" />
            </ListItem>
            <ListItem button onClick={() => navigate("/userPermission")}>
                <ListItemIcon>
                    <VisibilityOffIcon />
                </ListItemIcon>
                <ListItemText primary="User permission" />
            </ListItem> */}
            <ListItem button onClick={() => navigate("/enlistmentDate")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Enlistment Date setup" />
            </ListItem>
            <ListItem button onClick={() => navigate("/fieldSpecialization")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Field Specialization" />
            </ListItem>
            <ListItem button onClick={() => navigate("/medicalCategory")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Medical Category setup" />
            </ListItem>
            <ListItem button onClick={() => navigate("/medicalQuestionnaire")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Medical Questionnaire setup" />
            </ListItem>
            <ListItem button onClick={() => navigate("/resetUserPassword")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Reset User Password" />
            </ListItem>
            <ListItem button onClick={() => navigate("/trainingAcademyIntake")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Training Academy Intake" />
            </ListItem>
            <ListItem button onClick={() => navigate("/noticeConfiguration")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Notice Configuration" />
            </ListItem>
            <ListItem button onClick={() => navigate("/dzongkhagHospitalMapping")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Dzongkhag Hospital Mapping" />
            </ListItem>
            <ListItem button onClick={() => navigate("/hospitalScheduleTime")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Hospital Schedule Time" />
            </ListItem>
            <ListItem button onClick={() => navigate("/hospitalScheduleTimeList")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Hospital Schedule Time List" />
            </ListItem>
            <ListItem button onClick={() => navigate("/reason")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Reason" />
            </ListItem>
            {/* <ListSubheader inset> Test Forms</ListSubheader> */}
            {/* <ListItem button onClick={() => navigate("/screenDashboard")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Screen Dashboard" />
            </ListItem>
            <ListItem button onClick={() => navigate("/testForm")}>
                <ListItemIcon>
                    <AssignmentIcon />
                </ListItemIcon>
                <ListItemText primary="Test Form" />
            </ListItem> */}
        </div>
    )
}

export default SideMenu