import React, { useState, useEffect, useRef } from 'react';
import { useDispatch, useSelector, connect } from "react-redux";

import "jquery/dist/jquery.min.js";
import "datatables.net-dt/js/dataTables.dataTables";
import "datatables.net-dt/css/jquery.dataTables.min.css";
import "datatables.net-buttons/js/dataTables.buttons.js";
import "datatables.net-buttons/js/buttons.colVis.js";
import "datatables.net-buttons/js/buttons.flash.js";
import "datatables.net-buttons/js/buttons.html5.js";
import "datatables.net-buttons/js/buttons.print.js";
import $ from "jquery";

import userService from '../services/user.service';

const RegisteredUser = () => {
    const [registeredUsers, setRegisteredUsers] = useState(['']);
    const [content, setContent] = useState('');

    useEffect(() => {
        convertToDataTable();
        getRegisteredUsers();
    }, []);


    function getRegisteredUsers() {
        userService.getRegisteredUsers().then(
            response => {
                setRegisteredUsers(response.data);
            },
            error => {
                setContent(
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString()
                );
            }
        );
    }

    function convertToDataTable() {
        if (!$.fn.DataTable.isDataTable("#userTable")) {
            setTimeout(function () {
                $("#userTable").DataTable({
                    retrieve: true,
                    pagingType: "full_numbers",
                    pageLength: 20,
                    processing: true,
                    // sorting: false,
                    //   dom: "Bfrtip",
                    select: {
                        style: "single",
                    },
                    buttons: [
                        {
                            extend: "pageLength",
                            className: "btn btn-secondary bg-secondary",
                        },
                        {
                            extend: "copy",
                            className: "btn btn-secondary bg-secondary",
                        },
                        {
                            extend: "csv",
                            className: "btn btn-secondary bg-secondary",
                        },
                        {
                            extend: "print",
                            customize: function (win) {
                                $(win.document.body).css("font-size", "10pt");
                                $(win.document.body)
                                    .find("table")
                                    .addClass("compact")
                                    .css("font-size", "inherit");
                            },
                            className: "btn btn-secondary bg-secondary",
                        },
                    ],

                    fnRowCallback: function (
                        nRow,
                        aData,
                        iDisplayIndex,
                        iDisplayIndexFull
                    ) {
                        var index = iDisplayIndexFull + 1;
                        $("td:first", nRow).html(index);
                        return nRow;
                    },

                    lengthMenu: [
                        [10, 20, 30, 50, -1],
                        [10, 20, 30, 50, "All"],
                    ],
                    columnDefs: [
                        {
                            targets: 0,
                            render: function (data, type, row, meta) {
                                return type === "export" ? meta.row + 1 : data;
                            },
                        },
                    ],
                });
            }, 1000);
        }
    }

    const navigateToProfile = (userId) => {
        alert(userId)
    }
    function displayData() {
        try {
            return registeredUsers.map((item, index) => {
                return (
                    <tr key={item.userId} >
                        <td> {index + 1}</td>
                        <td> <span className='cursor-pointer link' onClick={() => navigateToProfile(item.userId)}>{item.fullName}</span></td>
                        <td> {item.gender == 'M' ? "Male" : "Female"}</td>
                        <td> {item.cid}</td>
                        <td> {item.email}</td>
                        <td> {item.mobileNo}</td>
                    </tr>
                );
            });
        } catch (e) {
            console.log(e.message);
        }
    };

    return (
        <div>
            {content ? (<div className="container">
                <header className="jumbotron">
                    <h3>{content}</h3>
                </header>
            </div>) : (
                // <div className='card'>
                //     <div className='card-body'>
                <div className='col-md-12 row'>
                    <div className='mb-2'>Registered users</div>
                    <div className="table-responsive">
                        <table id="userTable" className="table align-items-center justify-content-center mb-0 striped bordered hover responsive">
                            <thead>
                                <tr>
                                    <th>Sl. No</th>
                                    <th>Full Name</th>
                                    <th>Gender</th>
                                    <th>CID</th>
                                    <th>Email</th>
                                    <th>Mobile No</th>
                                </tr>
                            </thead>
                            <tbody>
                                {displayData()}
                            </tbody>
                        </table>
                    </div>
                </div>
                //     </div>
                // </div>
            )}
        </div>
    )
}

export default RegisteredUser;