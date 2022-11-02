import React from "react";
import Button from '@material-ui/core/Button';
import { Grid } from '@mui/material';
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import { makeStyles } from "@material-ui/core/styles";


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
const UserPermission = () => {
  const classes = useStyles();

  return (
    <>
      <div className="col-md-12 row">
        <div className="d-flex justify-content-between align-items-center">
          <Grid container>
            <Grid item xl={9} md={3}>
              <FormControl className={classes.formControl}>
                <InputLabel id="demo-simple-select-helper-label">Role</InputLabel>
                <Select
                  labelId="demo-simple-select-helper-label"
                  id="demo-simple-select-helper"
                >
                  <MenuItem value={1}><span>Admin</span></MenuItem>
                  <MenuItem value={2}><span>Doctor</span></MenuItem>
                  <MenuItem value={3}><span>Operator</span></MenuItem>
                  <MenuItem value={4}><span>Student</span></MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xl={6} md={6} sm={12} xs={12} padding={3}>
              <Button variant="contained" size="small" color="primary">
                Search
              </Button>
            </Grid>
          </Grid>
        </div>
      </div>
      <div className="">
        Role name: Operator
      </div>
    </>
  )
}

export default UserPermission