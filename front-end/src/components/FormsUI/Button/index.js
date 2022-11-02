import React from 'react';
import Controls from "../../controls/Controls";
import {useFormikContext} from 'formik';

const ButtonWrapper = ({
                           name,
                           buttonName,
                           ...otherProps
                       }) => {

    const {submitForm} = useFormikContext();

    const handleSubmit = () => {
        submitForm()
    }

    const configButton = {
        variant: 'contained',
        color: 'primary',
        //type:'submit',
        //fullWidth: true,
        size:"small",
        onClick: handleSubmit
    }

    return (
        <Controls.Button {...configButton} text={buttonName}>
        </Controls.Button>
    );
};

export default ButtonWrapper;