import React from 'react';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';
import Button from '@material-ui/core/Button';
import fileDownload from "js-file-download";
import enrolmentService from "../../services/enrolment.service";


const EnrolmentInstruction = () => {

    const downloadFile = async () => {
        let url = "D:/projects/gyalsung/gyalsung-devlopment/document/parent consent form sample.pdf";
        let fileName = "parent consent form sample.pdf";
        const response = await enrolmentService.downloadParentConsentForm(url);
        fileDownload(response.data, fileName);
    };

    return (
        <div className='text-muted'>
            <p>
                The National Service of Bhutan is a broad and profound concept which will have a
                transformative effect on the youth providing them with the opportunity to actualize
                their innate potential and unleash it in the service of Tsa-Wa-Sum.
            </p>
            <p>
                The Constitution of Kingdom of Bhutan, Article 8 on the Fundamental Duties states
                that a Bhutanese Citizen shall render National Service when called upon to do so by
                the Parliament. The Gyalsung Bill reaffirms the National Service Obligation whereby
                all 18 years Bhutanese youth must undergo Gyalsung Training, provide National Service
                Duty and serve as National Service Reservist until the age of 45.
            </p>
            <p>
                Considering the importance of the Gyalsung Program, vigorous sensitization and awareness
                programs will be carried out at all levels (schools, organizations, LG, etc) and through
                media channels.
            </p>
            <mark>
                If you are minor(below 18 years old from the enlistment day March 02, 2024 or born before Janauary 01, 2000) and wishes to enrol, you need to get consent from your parent/guardian.
                Please download and fill up the consent form and then upload soft copy in the family detail in step 3.
            </mark>
            <div className="mb-2">
                Consent form is available here. &nbsp;&nbsp;

                <Button variant="outlined" download size="small" color="primary"
                    onClick={downloadFile}>
                    <CloudDownloadIcon />  Download consent form
                </Button>
            </div>
        </div>
    )
}

export default EnrolmentInstruction;