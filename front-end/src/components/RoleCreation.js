import React, { useEffect, useState } from "react";
import Button from '@material-ui/core/Button';
import { DataGrid } from "@mui/x-data-grid";
import { makeStyles } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle'
import Slide from '@material-ui/core/Slide';
import { Link, Grid } from '@mui/material';
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
const RoleCreation = () => {
  const classes = useStyles();
  const [open, setOpen] = useState(false);


  const handleClose = () => {
    setOpen(false);
  };
  const rows = [
    { id: 1, col1: "1", col2: "Admin", col3: "Edit" },
    { id: 2, col1: "2", col2: "Operator", col3: "Edit" },
    { id: 3, col1: "3", col2: "Doctor", col3: "Edit" },
    { id: 4, col1: "4", col2: "Student", col3: "Edit" },
  ];

  const columns = [
    {
      field: 'col1',
      headerName: 'SL No',
      width: 100,
      disableClickEventBubbling: true,
    },
    {
      field: 'col2',
      headerName: 'Particular',
      width: 500,
    },
    {
      field: 'col3',
      headerName: 'Action',
      width: 100,
      disableClickEventBubbling: true,
      renderCell: () => {
        return (
          <Button variant="contained" size="small" color="primary" onClick={handleClose}>
            Edit
          </Button>
        )
      }
    }
  ]

  return (<div>
    <div className='col-md-12 row'>
      <div className="d-flex justify-content-between align-items-center">
        <strong className="text-muted text-sm-left mb-0 mb-sm-3">
          Role setup</strong>
        <Button variant="contained" size="small" color="primary"
          onClick={() => { setOpen(true) }}>
          Add new
        </Button>
      </div>
    </div>


    <div style={{ height: 300, width: "100%" }}>
      <br></br>
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={5}
        rowsPerPageOptions={[5]}
      // checkboxSelection
      />
    </div>
    <Dialog
      open={open}
      TransitionComponent={Transition}
      keepMounted
      onClose={handleClose}
      aria-labelledby="alert-dialog-slide-title"
      aria-describedby="alert-dialog-slide-description"
    >
      <DialogTitle id="alert-dialog-slide-title">{"User Role Creation"}</DialogTitle>
      <DialogContent>
        <div className="col-md-12">
          <TextField
            id="standard-textarea"
            label="Role name"
            required
          // value={cidNumber}
          // onChange={e => {
          //     setCidNumber(e.target.value)
          // }}
          />
        </div>
      </DialogContent>
      <DialogActions>
        <Button variant="contained" size="small" color="secondary"
          onClick={handleClose} >
          Cancel
        </Button>
        <Button variant="contained" size="small" color="primary"
          onClick={handleClose} >
          Add
        </Button>
      </DialogActions>
    </Dialog>
  </div>
  )
}

export default RoleCreation;