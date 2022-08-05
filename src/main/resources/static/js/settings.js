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
                    let {id, productId, productName, minimum, maximum} = row;
                    tableRows += `<tr class="d-flex">
                        <td class="col-1">${productId}</td>
                        <td class="col-2">${productName}</td>
                        <td class="col-2">${minimum}</td>
                        <td class="col-2">${maximum}</td>
                        <td class="col-1">
			                <a th:href="@{/deleteid/${id}}"><i class="fa fa-trash" style="color:tomato"></i>
			                </a>
			                <a th:href="@{/editid(notId=${id})}">
                                <i class="fa fa-pencil"></i>
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
