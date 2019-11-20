$(function () {

// ==================================================================================

    const MODAL = document.querySelector('.modal');
    const URL_BASE = 'product-images-uploads';
    const IMAGE_BY_ID_URL = '/product-images';
    const PRODUCT_JOIN_PRODUCT_IMAGES_URL = '/product-join-product-images';
// ==================================================================================

// HTML PAGE "product-images-uploads.html"
// SERVER: ProductImageUploadsController: uploadsTest(),
// GET url: "product-images-uploads/test"
    function testIt() {
        $('#testForm').click(function (event) {
            event.preventDefault();
            console.log('testIt submitted');
            $.ajax({
                type: 'get',
                url: 'product-images-uploads/test',
                dataType: 'text',
                success: function (data, status, jqXHR) {
                    console.log('test success');
                    $('#testMessage').html(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('test error');
                    $('#testMessage').html('test error');
                }
            })
        })
    }
// ---------------------------------------------------------------------------------------------------------
    /**
     * @param submitId
     * @param inputId
     * @param successTxtId
     * @param errorTxtId
     * @param uploadFileImgId
     * HTML PAGE "product-images-uploads.html" {@link singleUploadForm}
     * SERVER: ProductImageUploadsController: uploadFile(@RequestParam("file") MultipartFile file),
     * POST url: "product-images-uploads")
     */
    function uploadImage(submitId = 'singleFileUploadSubmit', inputId = 'singleFileUploadInput',
                         successTxtId = 'singleFileUploadSuccess', errorTxtId = 'singleFileUploadError',
                         uploadFileImgId = 'uploadFileImg') {
        let submit = $('#' + submitId);
        let input = $('#' + inputId);
        let successTxt = $('#' + successTxtId);
        let errorTxt = $('#' + errorTxtId);
        let uploadFileImg = $('#' + uploadFileImgId);

        submit.click(function (event) {
            event.preventDefault();
            let files = input.prop('files');
            if (!files[0]) {
                errorTxt.html('Please select a file');
            } else {
                let formData = new FormData();
                formData.append('file', files[0]);
                $.ajax({
                    type: 'POST',
                    url: window.location.href, // "http://localhost:8081/store/product-images-uploads"

//                  contentType (i.e. the type of data sent to the server) needs to be false for multipart file:
                    contentType: false,
                    cache: false,
                    processData: false,
//                  data sent to the server (a plain object or string):
                    data: formData,
// dataType is the type of data expected back from the server (that is the one I'm ready to accept and intend
// to work with)  (it's 'obtainedData' in success function).
// Some mess is here, I don't know why: I won't be able read the obtainedData properties, if I explicitly indicated the dataType here.
// Though actually the image is successfully saves in the db and I obtain json object from server.
// Everything goes well without dataType indication.
//                    dataType: 'application/json',
                    success: function (obtainedData, status, jqXHR) {
                        console.log('file ' + obtainedData.fileName + ' stored in db');
                        errorTxt.html('');
                        let fileHref = obtainedData.fileDownloadUri; // "http://localhost:8081/store/product-images-uploads/download/40"
// SERVER: ProductImageUploadsController:
// downloadFile(@PathVariable(value = "resourceId") Long resourceId, @Autowired HttpServletRequest request),
// GET url: 'product-images-uploads/download/{resourceId}'
                        successTxt.html('<a href="' + fileHref + '" download>' + fileHref + '</a>');
                        uploadFileImg.attr("src", fileHref);
                    }, error: function (jqXHR, textStatus, errorThrown) {

                    }
                });
            }
        })
    }

// ------------------------------------------------------------------------------------------------------------

    async function getProductImageById(imgId, errorTxtElement) {
        let response = await fetch(`${URL_BASE}${IMAGE_BY_ID_URL}/${imgId}`, {method: 'GET'});
        if(response.ok) return await response.json();
        else {
            errorTxtElement.innerHTML = `Some error occured: ${response.status}: ${response.statusText}`;
            errorTxtElement.style.display = 'block';
            return null;
        }
    }
// ------------------------------------------------------------------------------------------------------------

    /**
     * render table with results of ajax request of appropriate ProductImage instance
     * @param imgId - id of the ProductImage instance
     * @param errorTxtId - id of the error message block
     * @param imgDataTableId - id of the image data table block
     * @param formId - id of the find-image form
     */
    async function showProductImageById(imgId,
                                  errorTxtId = 'image-id-number-parse-error',
                                  imgDataTableId = 'show-product-images-table',
                                  formId = 'find-image-by-id-form') {
        let form = document.querySelector(`#${formId}`);
        let numberParseErrorTxt = document.querySelector('#' + errorTxtId);
        imgId = Number.parseInt(imgId, 10);
        // parseInt() returns number or NaN to be coerced to boolean true or false with unambiguous way:
        if (!imgId) {
            numberParseErrorTxt.innerHTML = 'Please type number id.';
            numberParseErrorTxt.style.display = 'block';
// after error text appeared, move the form field to the bottom position (arg false)
            form.scrollIntoView(false);
            return;
        }
        numberParseErrorTxt.innerHTML = '';
        numberParseErrorTxt.style.display = 'none';
        // create table with headers row:
        let table = document.querySelector('#' + imgDataTableId);
        // send request:
// TODO: create getPreview method and add preview here, full-sized one only on click
        let obtainedData = await getProductImageById(imgId, numberParseErrorTxt);
        if (obtainedData) appendProductImageShowTableRow(table, obtainedData);
        form.scrollIntoView(false);
    }
// ---------------------------------------------------------------------------------------------------------

    // TODO: to finish
    function showProductImageByName(productName, imgDataTableId = 'show-product-images-table',
                                        containerId = 'find-image-container') {
        // let container = document.createElement(containerId);
    }

// ---------------------------------------------------------------------------------------------------------

    /**
     * create table with headers row to render results of ajax request of a ProductImage instance
     * @param containerId
     * @param imgDataTableId
     * @return {HTMLElement}
     */
    function createProductImageShowTable(containerId = 'find-image-container',
                                         imgDataTableId = 'show-product-images-table') {
        let container = document.getElementById(containerId);
        let table = document.createElement('table'),
            thead = document.createElement('thead'),
            tbody = document.createElement('tbody');
        table.id = imgDataTableId;
// create and append table headers
        let headers = ['image id', 'image name', 'image type', 'product details id', 'image', 'hide'];
        for (let i = 0; i < headers.length; i++) {
            let th = document.createElement('th');
            th.innerHTML = headers[i];
            thead.append(th);
        }
        table.append(thead);
        table.append(tbody);
        container.append(table);
        return table;
    }

// ------------------------------------------------------------------------------------------------------------
    /**
     * display results of request for ProductImage data
     * @param table - table to display results
     * @param obtainedData - an object containing ProductImage data (id, fileName, fileType and image data byte array)
     * @param formId - id of the searching form
     */
    function appendProductImageShowTableRow(table, obtainedData, formId = '#find-image-by-id-form') {
        let tbody = table.children[1];
        let tr = document.createElement('tr'),
        img = document.createElement('img');
        // an array with obtainedData properties names to convenient adding table data
        let properties = ['id', 'fileName', 'fileType'];
        for (let i = 0; i < properties.length; i++) {
            let td = document.createElement('td');
            td.innerHTML = obtainedData[properties[i]];
            tr.append(td);
        }
        let td = document.createElement('td');
        let pDetailsId = obtainedData['productDetails'].map(item => item['id']);
        td.innerHTML = pDetailsId.join(', ');
        tr.append(td);
// https://stackoverflow.com/questions/20756042/javascript-how-to-display-image-from-byte-array-using-javascript-or-servlet
//        img.src = "data:image/jpeg;base64," + obtainedData['data'];
        // get image data not from database src, but directly from obtained byte array (that is 'data' property
        // of obtained ProductImage JSON object)
        img.src = "data:" + obtainedData['fileType'] + ";base64," + obtainedData['data'];
        img.alt = obtainedData.fileName;
        img.classList.add('img-table-cell');
        img.style.cursor = 'pointer';
         let imgTd = document.createElement('td');
         imgTd.append(img);
         tr.append(imgTd);

        let hideBtn = getHideButton();
        let hideTd = document.createElement('td');
        hideTd.append(hideBtn);
        tr.append(hideTd);
        tbody.append(tr);
    }

    // ---------------------------------------------------------------------------------------------------------

    function showProductJoinProductImageByNamePart(productName, dataTableId = 'show-product-join-images-table',
                                                   formId = 'find-product-form', errorTxtId = 'product-join-image-error') {
        let form = document.querySelector('#' + formId);
        let errorText = document.querySelector(`#${errorTxtId}`);
        errorText.style.display = 'none';
        // let url = new URL('http://localhost:8081/store/product-images-uploads/product-join-product-images');
        // if (productName) url.searchParams.set('name-part', productName);
        let url;
        if (!productName || productName.trim() === '') {
            errorText.innerHTML = 'Please put product name or name part';
            errorText.style.display = 'block';
            form.scrollIntoView(false);
            return;
        } else {
            url = `${URL_BASE}${PRODUCT_JOIN_PRODUCT_IMAGES_URL}?name-part=${productName.trim()}`;
        }
        let table = document.querySelector('#' + dataTableId);
        new Promise(function (resolve, reject) {
            let xhr = new XMLHttpRequest();
            xhr.open('GET', url, true);
            xhr.responseType = 'json';
            xhr.timeout = 10000;
            xhr.onload = function() {
                if(xhr.status >= 200 && xhr.status < 300) resolve(xhr.response);
                else reject({status: xhr.status, statustext: xhr.statusText});
            };
            xhr.onerror = () => reject(`An error occured: status ${xhr.status}, reason: ${xhr.statusText}`);
            xhr.send();
// since result is array of json objects
        }).then(value => value.forEach(item => appendProductJoinProductImageShowTableRow(table, item)))
            .then(() => // after table row appended, move the form field to the bottom position (arg false)
                form.scrollIntoView(false))
            .catch(reason => {
                errorText.innerHTML = `An error occured: status ${reason.status}, reason: ${reason.statusText}`;
                errorText.style.display = 'block';
                form.scrollIntoView(false);
            });
    }
// ------------------------------------------------------------------------------------------------------------

    /**
     * create table with headers row to render results of request of a ProductJoinProductImage instance
     * @param formId
     * @param dataTableId
     * @return {HTMLElement}
     */
    function createProductJoinProductImageShowTable(formId = 'find-product-form',
                                         dataTableId = 'show-product-join-images-table') {
        let form = document.querySelector('#' + formId);
        let table = document.createElement('table'),
            thead = document.createElement('thead'),
            tbody = document.createElement('tbody');
        table.id = dataTableId;
// create and append table headers
        let headers = ['product id', 'product name', 'producer id', 'product image id', 'product image name', 'product image', 'hide'];
        for (let i = 0; i < headers.length; i++) {
            let th = document.createElement('th');
            th.innerHTML = headers[i];
            thead.append(th);
        }
        table.append(thead);
        table.append(tbody);
        // container.append(table);
        form.before(table);
        return table;
    }
    // ------------------------------------------------------------------------------------------------------------
    /**
     * display results of request for ProductImage data
     * @param table - table to display results
     * @param obtainedData - an object containing ProductImage data (id, fileName, fileType and image data byte array)
     * @param errorTxtId
     */
        async function appendProductJoinProductImageShowTableRow(table, obtainedData,
                                                                 errorTxtId = 'product-join-image-error') {
            let tbody = table.children[1];
            let tr = document.createElement('tr'),
            img = document.createElement('img');
// create array with obtainedData properties names to convenient adding table data
            let properties = ['productId', 'productName', 'producerId', 'productImageId', 'productImageName'];
            for (let i = 0; i < properties.length; i++) {
                let td = document.createElement('td');
                td.innerHTML = obtainedData[properties[i]];
                tr.append(td);
            }
// get productImage
//             console.log(obtainedData['productImageData']);
            let imgSrc = "data:" + obtainedData['fileType'] + ";base64," + obtainedData['productImageData'];
            img.src = imgSrc;
                img.alt = obtainedData.fileName;
            img.classList.add('img-table-cell');
            img.style.cursor = 'pointer';
             let imgTd = document.createElement('td');
// get modal window on image click
             img.onclick = async (event) => {
                 // stop bubbling to prevent doOnclicks() window.onclick = (event) => MODAL.style.display = 'none';
                 event.stopPropagation();
                 // MODAL.classList.toggle('active');
                MODAL.style.display = 'flex';
                 let modalImage = document.querySelector('.modal-image');
                 modalImage.alt = obtainedData['productImageName'];
// set preview image src to modal-image src
                 modalImage.src = imgSrc;
// after loading, set full-sized image src to modal-image src
                 let fullImageJson = await getProductImageById(obtainedData['productImageId'],
                     document.querySelector(`#${errorTxtId}`));
                 modalImage.src = `data:${fullImageJson['fileType']};base64,${fullImageJson['data']}`;
 // get ProductImage instance with service method findById()
//                 getModalImageFullSize(obtainedData['productImageId'])
//                     .then(resolve => imgSrc = "data:" + resolve['fileType'] + ";base64," + resolve['data']);
             };

             imgTd.append(img);
             tr.append(imgTd);
            let hideBtn = getHideButton();
            let hideTd = document.createElement('td');
            hideTd.append(hideBtn);
            tr.append(hideTd);
            tbody.append(tr);
        }
// ------------------------------------------------------------------------------------------------------------

    /**
     * create 'hide' button to remove the current table row
     */
    function getHideButton() {
        let hideBtn = document.createElement('input');
        hideBtn.type = 'button';
        hideBtn.value = 'hide';
        // hideBtn.classList.add('hide-product-img-row');
        hideBtn.onclick = (event) => {
            event.preventDefault();
            hideBtn.parentElement.parentElement.innerHTML = '';
        };
        return hideBtn;
    }
// ------------------------------------------------------------------------------------------------------------

    function doSubmits() {
// find-image-by-id-form submit:
        document.querySelector('#find-image-by-id-form').onsubmit =
            (event) => {
                event.preventDefault();
                let findImageByIdInput = document.querySelector('#find-image-by-id-input');
                showProductImageById(findImageByIdInput.value);
                findImageByIdInput.value = '';
            };
// find-image-by-name-form submit:
        document.querySelector('#find-image-by-name-form').onsubmit =
            (event) => {
                event.preventDefault();
                let findImageByNameInput = document.querySelector('#find-image-by-name-input');
                // showProductImageByNamePart(findImageByNameInput.value);
                findImageByNameInput.value = '';
            };
// find-product-form submit:
        document.querySelector('#find-product-form').onsubmit =
            (event) => {
                event.preventDefault();
                let findProductByNameInput = document.querySelector('#find-product-input');
                showProductJoinProductImageByNamePart(findProductByNameInput.value);
                findProductByNameInput.value = '';
            };
    }
// ------------------------------------------------------------------------------------------------------------

function doOnclicks() {
// window onclick behavior
    window.onclick = (event) => MODAL.style.display = 'none';
// button 'Hide results' in the "find-image-by-name-form" onclick behavior
    document.querySelector('#hide-find-image-results').onclick = (event) =>
        document.querySelector('#show-product-images-table>tbody').innerHTML = '';
// button 'Hide results' in the "find-product-form" onclick behavior
    document.querySelector('#hide-find-product-results').onclick = (event) =>
        document.querySelector('#show-product-join-images-table>tbody').innerHTML = '';
}
// ------------------------------------------------------------------------------------------------------------

    function initializePage() {
        testIt();
        uploadImage('singleFileUploadSubmit', 'singleFileUploadInput',
            'singleFileUploadSuccess', 'singleFileUploadError', 'uploadFileImg');
        createProductImageShowTable('find-image-container', 'show-product-images-table');
        createProductJoinProductImageShowTable('find-product-form', 'show-product-join-images-table');
        doSubmits();
        doOnclicks();
    }
// ------------------------------------------------------------------------------------------------------------



// ================================================================ P E R F O R M A N C E

    // testIt();
    // showProductImageById('289');
    // uploadImage('singleFileUploadSubmit', 'singleFileUploadInput',
    //     'singleFileUploadSuccess', 'singleFileUploadError', 'uploadFileImg');
    initializePage();

});

// =========================================================================================================== CODE END
// ====================================================================================================================
