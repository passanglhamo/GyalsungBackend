import PageHeader from "../../src/components/PageHeader";
import React, { useEffect, useState } from "react";
import Button from '@material-ui/core/Button';
import { DataGrid } from "@mui/x-data-grid";
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import { makeStyles } from '@material-ui/core/styles';
import Avatar from '@material-ui/core/Avatar';
import Chip from '@material-ui/core/Chip';
import FaceIcon from '@material-ui/icons/Face';
import DoneIcon from '@material-ui/icons/Done';
import Grid from '@material-ui/core/Grid';
import Slide from '@material-ui/core/Slide';
import { useNavigate } from 'react-router-dom';
import bondeymaImg from '../images/p2.png';
import jamtsholingImg from '../images/bg1.jpg';
import tareyThangImg from '../images/k3.jpg';
import khotakhaImg from '../images/k6.jpg';
import pemathangImg from '../images/k2.jpg';

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
const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});
const TrainingAcademyDashboard = () => {
  const classes = useStyles();
  const navigate = useNavigate();

  return (
    <Grid container wrap="nowrap" spacing={2}>
      <Grid item xs={12}>
        <div className='col-md-12 row'>
          <div className='col-md-4'>
            <FormControl className={classes.formControl}>
              <InputLabel id="demo-simple-select-helper-label">Year</InputLabel>
              <Select labelId="demo-simple-select-helper-label" id="demo-simple-select-helper">
                <MenuItem value={1}><span>2023</span></MenuItem>
                <MenuItem value={2}><span>2024</span></MenuItem>
                <MenuItem value={3}><span>2025</span></MenuItem>
                <MenuItem value={4}><span>2026</span></MenuItem>
                <MenuItem value={5}><span>2027</span></MenuItem>
              </Select>
            </FormControl>
          </div>

          <div className='col-md-4'>
            <Button variant="contained" color="primary" >
              Check
            </Button>
          </div>
        </div>
        <div className='col-md-12 mb-2'>
          <div className='row'>
            <div className='col-md-4'>
              <div className="profile-widget">
                <div className="doc-img">
                  <a href="/academyWiseEnrolment">
                    <img src={bondeymaImg} alt="img" className="img-fluid" />
                  </a>
                </div>
                <div className="pro-content">
                  <h3 className="title">
                    Bondeyma(Gyelposhing)
                  </h3>
                  <p className="speciality">2 battalions</p>
                  <div className="row d-flex justify-content-between align-items-center">
                    <div className="col-auto">
                      <ul className="list-unstyled">
                        <li>
                          <i className="fas fa-map-marker-alt"></i> 1,670 Male
                        </li>
                        <li>
                          <i className="far fa-clock"></i> 2,789 Female
                        </li>
                        <li>
                          <i className="far fa-money-bill-alt"></i> Total  4,459
                        </li>
                      </ul>
                    </div>
                    <div className="col">
                      <Button variant="contained" color="primary" onClick={() => navigate("/academyWiseEnrolment")}> View details </Button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className='col-md-4'>
              <div className="profile-widget">
                <div className="doc-img">
                  <a href="/academyWiseEnrolment">
                    <img src={jamtsholingImg} alt="img" className="img-fluid" />
                  </a>
                </div>
                <div className="pro-content">
                  <h3 className="title">
                    Jamtsholing(Samtse)
                  </h3>
                  <p className="speciality">7 battalions</p>

                  <div className="row d-flex justify-content-between align-items-center">
                    <div className="col-auto">
                      <ul className="list-unstyled">
                        <li>
                          <i className="fas fa-map-marker-alt"></i> 1,400 Male
                        </li>
                        <li>
                          <i className="far fa-clock"></i> 1,450 Male
                        </li>
                        <li>
                          <i className="far fa-money-bill-alt"></i> Total  6,459
                        </li>
                      </ul>
                    </div>
                    <div className="col">
                      <Button variant="contained" color="primary" onClick={() => navigate("/academyWiseEnrolment")}> View details </Button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className='col-md-4'>
              <div className="profile-widget">
                <div className="doc-img">
                  <a href="/academyWiseEnrolment">
                    <img src={khotakhaImg} alt="img" className="img-fluid" />
                  </a>
                </div>
                <div className="pro-content">
                  <h3 className="title">
                    Khotokha(Wangduephodrang)
                  </h3>
                  <p className="speciality">2 battalions</p>

                  <div className="row d-flex justify-content-between align-items-center">
                    <div className="col-auto">
                      <ul className="list-unstyled">
                        <li>
                          <i className="fas fa-map-marker-alt"></i> 1,400 Male
                        </li>
                        <li>
                          <i className="far fa-clock"></i> 1,450 Male
                        </li>
                        <li>
                          <i className="far fa-money-bill-alt"></i> Total  6,459
                        </li>
                      </ul>
                    </div>
                    <div className="col">
                      <Button variant="contained" color="primary"> View details </Button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className='col-md-12 mb-2'>
          <div className='row'>
            <div className='col-md-6'>
              <div className="profile-widget">
                <div className="doc-img">
                  <a href="/academyWiseEnrolment">
                    <img src={pemathangImg} alt="img" className="img-fluid" />
                  </a>
                </div>
                <div className="pro-content">
                  <h3 className="title">
                    Pemathang(Samdrupjongkhar)
                  </h3>
                  <p className="speciality">7 battalions</p>

                  <div className="row d-flex justify-content-between align-items-center">
                    <div className="col-auto">
                      <ul className="list-unstyled">
                        <li>
                          <i className="fas fa-map-marker-alt"></i> 1,400 Male
                        </li>
                        <li>
                          <i className="far fa-clock"></i> 1,450 Male
                        </li>
                        <li>
                          <i className="far fa-money-bill-alt"></i> Total  6,459
                        </li>
                      </ul>
                    </div>
                    <div className="col">
                      <Button variant="contained" color="primary" onClick={() => navigate("/academyWiseEnrolment")}> View details </Button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className='col-md-6'>
              <div className="profile-widget">
                <div className="doc-img">
                  <a href="/academyWiseEnrolment">
                    <img src={tareyThangImg} alt="img" className="img-fluid" />
                  </a>
                </div>
                <div className="pro-content">
                  <h3 className="title">
                    Tareythang(Gelephu)
                  </h3>
                  <p className="speciality">7 battalions</p>

                  <div className="row d-flex justify-content-between align-items-center">
                    <div className="col-auto">
                      <ul className="list-unstyled">
                        <li>
                          <i className="fas fa-map-marker-alt"></i> 1,400 Male
                        </li>
                        <li>
                          <i className="far fa-clock"></i> 1,450 Male
                        </li>
                        <li>
                          <i className="far fa-money-bill-alt"></i> Total  6,459
                        </li>
                      </ul>
                    </div>
                    <div className="col">
                      <Button variant="contained" color="primary" onClick={() => navigate("/academyWiseEnrolment")}> View details </Button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Grid>
    </Grid>
  )
}

export default TrainingAcademyDashboard