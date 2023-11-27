const bookForm = document.getElementById('bookForm');
const eCheckBoxAuthors = document.getElementsByName('authors');
const tBody = document.getElementById("tBody");
const eSelectType = document.getElementById('type');
const ePagination = document.getElementById('pagination')
const eSearch = document.getElementById('search');
const eModalTitle = document.getElementById("staticBackdropLabel");
const submitBtn = document.getElementById("submit-btn");
// const eOptionsType = eSelectType.querySelectorAll("option");
const formBody = document.getElementById('formBody');
const eHeaderPrice = document.getElementById('header-price')

let bookSelected = {};
let roomDetail;
let pageable = {
    page: 1,
    sort: 'id,desc',
    search: ''
}
let categories;
let authors;
let books = [];

bookForm.onsubmit = async (e) => {
    e.preventDefault();
    let data = getDataFromForm(bookForm);
    data = {
        ...data,
        category: {
            id: data.category
        },
        idAuthors: Array.from(eCheckBoxAuthors)
            .filter(e => e.checked)
            .map(e => e.value),
        id: bookSelected.id
    }

    if (bookSelected.id) {
        await editBook(data);
        webToast.Success({
            status: 'Sửa thành công',
            message: '',
            delay: 2000,
            align: 'topright'
        });
    } else {
        await createBook(data)
        webToast.Success({
            status: 'Thêm thành công',
            message: '',
            delay: 2000,
            align: 'topright'
        });
    }
    await renderTable();
    $('#staticBackdrop').modal('hide');

}
function getDataFromForm(form) {
    // event.preventDefault()
    const data = new FormData(form);
    return Object.fromEntries(data.entries())
}
async function getAuthorsSelectOption() {
    const res = await fetch('api/authors');
    return await res.json();
}
async function getCategoriesSelectOption() {
    const res = await fetch('api/categories');
    return await res.json();
}
async function getList() {
    const response = await fetch(`/api/books?page=${pageable.page - 1 || 0}&sort=${pageable.sortCustom || 'id,asc'}&search=${pageable.search || ''}`);
    //response có status ok hoặc không, header và body
    //{page: 1, size: 10, content: []}
    //{ size: 15, content: [1,2,3]}
    //{page:1 , size: 15, content: [1,2,3]}

    if (!response.ok) {
        // Xử lý trường hợp không thành công ở đây, ví dụ: throw một lỗi hoặc trả về một giá trị mặc định
        throw new Error("Failed to fetch data");
    }

    const result = await response.json();
    pageable = {
        ...pageable,
        ...result
    };
    genderPagination();
    renderTBody(result.content);
    return result; // Trả về kết quả mà bạn đã lấy từ response.json()
    // addEventEditAndDelete();
}
function renderTBody(items) {
    let str = '';
    items.forEach(e => {
        str += renderItemStr(e);
    })
    tBody.innerHTML = str;
}
const genderPagination = () => {
    ePagination.innerHTML = '';
    let str = '';
    //generate preview truoc
    str += `<li class="page-item ${pageable.first ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
            </li>`
    //generate 1234

    for (let i = 1; i <= pageable.totalPages; i++) {
        str += ` <li class="page-item ${(pageable.page) === i ? 'active' : ''}" aria-current="page">
      <a class="page-link" href="#">${i}</a>
    </li>`
    }
    //
    //generate next truoc
    str += `<li class="page-item ${pageable.last ? 'disabled' : ''}">
              <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Next</a>
            </li>`
    //generate 1234
    ePagination.innerHTML = str;

    const ePages = ePagination.querySelectorAll('li'); // lấy hết li mà con của ePagination
    const ePrevious = ePages[0];
    const eNext = ePages[ePages.length-1]

    ePrevious.onclick = () => {
        if(pageable.page === 1){
            return;
        }
        pageable.page -= 1;
        getList();
    }
    eNext.onclick = () => {
        if(pageable.page === pageable.totalPages){
            return;
        }
        pageable.page += 1;
        getList();
    }
    for (let i = 1; i < ePages.length - 1; i++) {
        if(i === pageable.page){
            continue;
        }
        ePages[i].onclick = () => {
            pageable.page = i;
            getList();
        }
    }
}
const onSearch = (e) => {
    e.preventDefault()
    pageable.search = eSearch.value;
    pageable.page = 1;
    getList();
}
const searchInput = document.querySelector('#search');
searchInput.addEventListener('search', () => {
    onSearch(event)
});
const onLoadSort = () => {
    eHeaderPrice.onclick = () => {
        let sort = 'price,desc'
        const chevronDown = document.querySelector('.bx-chevron-down');
        const chevronUp = document.querySelector('.bx-chevron-up');
        chevronDown.style.display = 'block';
        chevronUp.style.display = 'none';
        if(pageable.sortCustom?.includes('price') &&  pageable.sortCustom?.includes('desc')){
            sort = 'price,asc';
            chevronUp.style.display = 'block';
            chevronDown.style.display = 'none';
        }
        pageable.sortCustom = sort;
        getList();
    }
}
function renderItemStr(item) {
    return `<tr>
                    <td>
                        ${item.id}
                    </td>
                    <td title="${item.description}">
                        ${item.title}
                    </td>
                    <td>
                        ${item.description}
                    </td>
                    <td>
                        ${item.publishDate}
                    </td>
                    <td>
                        ${item.price}
                    </td>
                    <td>
                        ${item.type}
                    </td>
                    <td>
                        ${item.category}
                    </td>
                    <td>
                        ${item.bookAuthor}
                    </td>
                    <td>
                         <div class="dropdown">
                             <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                             <i class="bx bx-dots-vertical-rounded"></i>
                            </button>
                        <div class="dropdown-menu">
                        <button class="dropdown-item" onclick="showEdit(${item.id})"
                        data-bs-toggle="modal" data-bs-target="#staticBackdrop"
                        ><i class="bx bx-edit-alt me-1"></i> Edit</button
                            >
                        <button class="dropdown-item" onclick="deleteBook(${item.id})"
                        ><i class="bx bx-trash me-1"></i> Delete</button
                        >
              </div>
            </div>
                    </td>
                </tr>`
}
window.onload = async () => {
    authors = await getAuthorsSelectOption();
    categories = await getCategoriesSelectOption();
    await renderTable()
    onLoadSort();
    renderForm(formBody, getDataInput());
}
function getDataInput() {
    return [
        {
            label: 'Title',
            name: 'title',
            value: bookSelected.title,
            required: true,
            pattern: "^[A-Za-z ]{6,20}",
            message: "Title must have minimum is 6 characters and maximum is 20 characters",
        },
        {
            label: 'Price',
            name: 'price',
            value: bookSelected.price,
            pattern: "[1-9][0-9]{1,10}",
            message: 'Price errors',
            required: true
        },
        {
            label: 'Type',
            name: 'type',
            value: bookSelected.type,
            type: 'select',
            required: true,
            options: [{value: "SINGLE_VOLUME", name: "SINGLE_VOLUME"}, {value: "MULTIPLE_VOLUMES", name:"MULTIPLE_VOLUMES"}],
            message: 'Please choose Type'
        },
        {
            label: 'Publish Date',
            name: 'publishDate',
            value: bookSelected.publishDate,
            type: 'date',
            required: true,
            message: 'Please choose Date'
        },
        {
            label: 'Description',
            name: 'description',
            value: bookSelected.description,
            pattern: "^[A-Za-z ]{6,120}",
            message: "Description must have minimum is 6 characters and maximum is 20 characters",
            required: true
        },
        {
            label: 'Category',
            name: 'category',
            value: bookSelected.categoryId,
            type: 'select',
            required: true,
            options: categories,
            message: 'Please choose Category'
        },
    ];
}
async function renderTable() {
    const pageable = await getList();
    books = pageable.content;
    renderTBody(books);
    addEventEditAndDelete();
}
const findById = async (id) => {
    const response = await fetch('/api/books/' + id);
    return await response.json();
}
function showCreate() {
    $('#staticBackdropLabel').text('Create Book');
    clearForm();
    renderForm(formBody, getDataInput())
}
async function showEdit(id) {
    $('#staticBackdropLabel').text('Edit Book');
    clearForm();
    bookSelected = await findById(id);
    bookSelected.authorIds.forEach(idAuthor => {
        for (let i = 0; i < eCheckBoxAuthors.length; i++) {
            if (idAuthor === +eCheckBoxAuthors[i].value) {
                eCheckBoxAuthors[i].checked = true;
            }
        }
    })
    renderForm(formBody, getDataInput());
}
function clearForm() {
    bookForm.reset();
    bookSelected = {};
}
async function editBook(data) {
    const res = await fetch('/api/books/' + data.id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
}
async function deleteBook(id) {
    const confirmBox = webToast.confirm("Are you sure to delete Book " + id + "?");
    confirmBox.click(async function () {
        const res = await fetch('/api/books/' + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(id)
        });
        if (res.ok) {
            // alert("Deleted");
            webToast.Success({
                status: 'Xóa thành công',
                message: '',
                delay: 2000,
                align: 'topright'
            });
            await getList();
        } else {
            alert("Something went wrong!")
        }
    });
}
async function createBook(data) {
    const res = await fetch('/api/books', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
}
const addEventEditAndDelete = () => {
    const eEdits = tBody.querySelectorAll('.edit');
    const eDeletes = tBody.querySelectorAll('.delete');
    for (let i = 0; i < eEdits.length; i++) {
        console.log(eEdits[i].id)
        eEdits[i].addEventListener('click', () => {
            showEdit(eEdits[i].dataset.id);
        })
    }
}

