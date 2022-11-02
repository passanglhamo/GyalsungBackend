import PageHeader from "../../src/components/PageHeader";
import React, { useEffect, useState } from "react";
import Button from '@material-ui/core/Button';
import { DataGrid } from "@mui/x-data-grid";
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import { makeStyles } from '@material-ui/core/styles';
import Avatar from '@material-ui/core/Avatar';
import GroupIcon from '@material-ui/icons/Group';
import EmojiPeopleIcon from '@material-ui/icons/EmojiPeople';
import GroupAddIcon from '@material-ui/icons/GroupAdd';
import Slide from '@material-ui/core/Slide';
import { useNavigate } from 'react-router-dom';
import UpdateIcon from '@material-ui/icons/Update';
import DoubleLineCharts from "./charts/apex/DoubleLineCharts";
import Pie from "./charts/apex/Pie";
import DoubleLineAreaCharts from "./charts/apex/DoubleLineAreaCharts";

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
}));
const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});
const GeneralDashboard = () => {
  const classes = useStyles();
  const navigate = useNavigate();

  return (<div>
    <div className='col-md-12 row'>
      <div className={classes.root}>
        <div className='row'>
          <div className='col-md-3'>
            <FormControl className={classes.formControl}>
              <InputLabel id="demo-simple-select-helper-label">Year</InputLabel>
              <Select
                labelId="demo-simple-select-helper-label"
                id="demo-simple-select-helper" >
                <MenuItem value={1}><span>2023</span></MenuItem>
                <MenuItem value={2}><span>2024</span></MenuItem>
                <MenuItem value={3}><span>2025</span></MenuItem>
                <MenuItem value={4}><span>2026</span></MenuItem>
                <MenuItem value={5}><span>2027</span></MenuItem>
              </Select>
            </FormControl>
          </div>
          <div className='col-md-2'>
            <Button variant="contained" color="primary">  Check </Button>
          </div>
          <div className='col-md-7 text-end muted-text cursor-pointer'
            onClick={() => navigate("/registeredUser")}>
            <GroupAddIcon /> Registered users: <span>230</span> <ChevronRightIcon />
          </div>
        </div>
      </div>
    </div>
    <div className='col-md-12'>
      <div className='row'>
        <div className='col-md-3'>
          <div className="profile-widget bg-yellow">
            <div className="row align-items-center text-muted mb-2">
              <div className="col-auto">
                <Avatar>
                  <GroupIcon />
                </Avatar>
              </div>
              <div className="col text-white">
                Eligible population for Gyalsung in 2023
              </div>
            </div>
            <div className="row align-items-center text-white">
              <div className="col-auto">
                Male: 2409
              </div>
              <div className="col-auto">
                Male: 2409
              </div>
              <div className="col-auto">
                Total: 2409
              </div>
              <div className="col-auto">
                <Button variant="outlined" size="small" color="primary" onClick={() => navigate("/academyWiseEnrolment")}> View details </Button>
              </div>
            </div>
          </div>
        </div>
        <div className='col-md-3'>
          <div className="profile-widget bg-orange">
            <div className="row align-items-center text-muted mb-2">
              <div className="col-auto">
                <Avatar>
                  <GroupAddIcon />
                </Avatar>
              </div>
              <div className="col">
                Registered population for Gyalsung in 2023
              </div>
            </div>
            <div className="row align-items-center text-muted">
              <div className="col-auto">
                Male: 2409
              </div>
              <div className="col-auto">
                Male: 2409
              </div>
              <div className="col-auto">
                Total: 2409
              </div>
              <div className="col-auto">
                <Button variant="outlined" size="small" color="primary" onClick={() => navigate("/academyWiseEnrolment")}> View details </Button>
              </div>
            </div>
          </div>
        </div>

        <div className='col-md-3'>
          <div className="profile-widget bg-red">
            <div className="row align-items-center text-muted mb-2">
              <div className="col-auto">
                <Avatar>
                  <UpdateIcon />
                </Avatar>
              </div>
              <div className="col">
                Total population deferred in 2023
              </div>
            </div>
            <div className="row align-items-center text-muted">
              <div className="col-auto">
                Male: 2409
              </div>
              <div className="col-auto">
                Male: 2409
              </div>
              <div className="col-auto">
                Total: 2409
              </div>
              <div className="col-auto">
                <Button variant="outlined" size="small" color="primary" onClick={() => navigate("/academyWiseEnrolment")}> View details </Button>
              </div>
            </div>
          </div>
        </div>
        <div className='col-md-3'>
          <div className="profile-widget bg-green">
            <div className="row align-items-center text-muted mb-2">
              <div className="col-auto">
                <Avatar>
                  <EmojiPeopleIcon />
                </Avatar>
              </div>
              <div className="col">
                Total population exempted in 2023
              </div>
            </div>
            <div className="row align-items-center text-muted">
              <div className="col-auto">
                Male: 2409
              </div>
              <div className="col-auto">
                Male: 2409
              </div>
              <div className="col-auto">
                Total: 2409
              </div>
              <div className="col-auto">
                <Button variant="outlined" size="small" color="primary" onClick={() => navigate("/academyWiseEnrolment")}> View details </Button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div className='col-md-12 row'>
      <div className='col-md-6'>
        <DoubleLineCharts />
      </div>
      <div className='col-md-6'>
        <DoubleLineAreaCharts />
      </div>
      <div className='col-md-6'>
        <Pie />
      </div>
    </div>
  </div>
  )
}

export default GeneralDashboard;