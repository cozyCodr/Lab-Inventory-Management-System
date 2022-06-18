// https://github.com/rootVIII/searchable_bootstrap_table

// noinspection JSValidateTypes
class SearchableTable {
    constructor(tableData) {
        this.tableData = tableData;
        this.tableBody = document.getElementById('tableBody');
        this.searchBox = document.getElementById('searchBox');
        document.addEventListener('keyup', this.updateTable.bind(this), false);
    }

    loadTableData(searchText = null) {
        return new Promise((resolve) => {
            let tableRows = '';
            for (let row of this.tableData) {
                if (!(searchText) || row.join(' ').includes(searchText)) {
                    let {id, productid, productname} = row;
                    tableRows += `<tr class="d-flex">
                        <td class="col-1">${productid}</td>
                        <td class="col-2">${productname}</td>
                        <td class="col-1">
			                <a href="delete/${id}"><i class="fa fa-trash" style="color:tomato"></i>
			                </a>
			            </td>
                        </tr>`;
                }
            }
            resolve(tableRows);
        });
    }

    init_table() {
        this.loadTableData().then((tableRows) => {
            this.tableBody.innerHTML = tableRows;
        }).catch((err) => {
            console.log(err);
        });
    }

    updateTable() {
        const inputText = this.searchBox.value.trim();
        if (!(inputText.length)) {
            this.init_table();
        } else {
            this.loadTableData(inputText).then((tableRows) => {
                this.tableBody.innerHTML = tableRows;
            }).catch((err) => {
                console.log(err);
            });
        }
    }
}

function getTableData() {
    // simulate fetching table data from the back-end:
    let productListCopy = productList;
    return productListCopy;
}

function main() {
    let searchableTable;

    document.addEventListener('DOMContentLoaded', () => {
        searchableTable = new SearchableTable(getTableData());
        searchableTable.init_table();
    });
}

main();
