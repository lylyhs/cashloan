import React from 'react'
import {Table, Modal, Icon} from 'antd';
import Lookdetails from "./Lookdetails";
const objectAssign = require('object-assign');
import AddWin from "./AddWin";
var confirm = Modal.confirm;
export default React.createClass({
    getInitialState() {
        return {
            selectedRowKeys: [], // 这里配置默认勾选列
            loading: false,
            data: [],
            pagination: {
                pageSize: 10,
                current: 1
            },
            canEdit: true,
            visible: false,
            visible1: false,
            visible2: false,
            pictureData: [],
            creditReportData: [],
            rowRecord:[],
            record:"",
            visibleAdd:false,

        };
    },

    componentWillReceiveProps(nextProps, nextState) {
        this.fetch(nextProps.params);
    },

    componentDidMount() {
        this.fetch();
        []
    },

    fetch(params = {}) {
        this.setState({
            loading: true
        });
        if (!params.pageSize) {
            var params = {};
            params = {
                pageSize: 10,
                current: 1,
            }
        }
        Utils.ajaxData({
            url: '/modules/manage/pay/log/page.htm',
            method: "post",
            data: params,
            callback: (result) => {
                const pagination = this.state.pagination;
                pagination.current = params.current;
                pagination.pageSize = params.pageSize;
                pagination.total = result.page.total;
                if (!pagination.current) {
                    pagination.current = 1
                };
                this.setState({
                    loading: false,
                    data: result.data,
                    pagination
                });
            }
        });
    },

    //查看弹窗
    showModal(title,record, canEdit) {
        var me = this;
      Utils.ajaxData({
            url: '/pay/testPayNotify.htm ',
            method: "post",
            data: {
                orderNo:record.orderNo
            },
            callback: (result) => {
                if (result.code == 200) {
                    Modal.success({
                        title: result.msg,
                        onOk: () => {
                            me.hideModal();
                        }
                    });
                } else {
                    Modal.error({
                        title: result.msg
                    });
                }
            }
        });
        
    },
  //新增
  addModal(title, record, canEdit){
      this.setState({
        visibleAdd:true,
        title:title,  
      })

  },
    //隐藏弹窗
    hideModal() {
        this.setState({
            visible: false,
            visible1: false,
            visible2: false,
            selectedIndex: '',
            selectedRowKeys: [],
            visibleAdd:false
        });
        this.refreshList();
    },
    rowKey(record) {
        return record.id;
    },

    //选择
    onSelectChange(selectedRowKeys) {
        this.setState({
            selectedRowKeys
        });
    },

    //分页
    handleTableChange(pagination, filters, sorter) {
        const pager = this.state.pagination;
        pager.current = pagination.current;
        this.setState({
            pagination: pager,
        });
        this.refreshList();
    },

    refreshList() {
        var pagination = this.state.pagination;
        var params = objectAssign({}, this.props.params, {
            current: pagination.current,
            pageSize: pagination.pageSize,
        });
        this.fetch(params);
    },

    //行点击事件
    onRowClick(record) {
        //console.log(record)
        this.setState({
            selectedRowKeys: [record.id],
            selectedRow: record,
            rowRecord:record
        },()=>{
            this
        });
    },

    // 列表添加选中行样式
    rowClassName(record) {
        let selected = this.state.selectedIndex;
        //console.log('selected', this.state.selectedIndex)
        return (record.id == selected && selected !== '') ? 'selectRow' : '';

    },

    //选择
    onSelectAll(selected, selectedRowKeys, selectedRows, changeRows) {
        if (selected) {
            this.setState({
                selectedRowKeys
            })
        } else {
            this.setState({
                selectedRowKeys: []
            })
        }
    },
    
    checkPayModal(record,title) {
        var record=record;
        console.log(record)
        var me = this;
        var status;
        var msg = "查询成功";
        var tips = "您是否查询支付结果";
        var trueurl = "/modules/manage/borrow/repay/check.htm";
        confirm({
            title: tips,
            onOk: function() {
                Utils.ajaxData({
                    url: trueurl,
                    data: {
                    	borrowId: record.borrowId,
                    },
                    method: 'post',
                    callback: (result) => {
                        if (result.code == 200) {
                            Modal.success({
                                title: result.msg,
                            });
                            me.refreshList();
                        } else {
                           Modal.error({
                                title: result.msg,
                            });
                        }
                        
                    }
                });
            },
            onCancel: function() { }
        });
    },

    render() {
        const {
            loading,
            selectedRowKeys
            } = this.state;
        const rowSelection = {
            type: 'checkbox',
            selectedRowKeys,
            onSelectAll: this.onSelectAll,
        };
        let me=this;
        const hasSelected = selectedRowKeys.length > 0;
        let openEdit = true;
        if (hasSelected && selectedRowKeys.indexOf("0") === -1) {
            openEdit = false;
        }
        var columns = [{
            title: '收款人姓名',
            dataIndex: 'realName'
        }, {
            title: '手机号码',
            dataIndex: 'phone'
        }, {
            title: '金额',
            dataIndex: 'amount'
        }, {
            title: '收款银行卡',
            dataIndex: 'cardNo'
        }, {
            title: '支付订单号',
            dataIndex: 'orderNo'
        }, {
            title: '借款时间',
            dataIndex: "loanTime"
        }, {
            title: '打款时间',
            dataIndex: 'updateTime'
        }, {
            title: '业务',
            dataIndex: "scenesStr",
        }, {
            title: '状态',
            dataIndex: "stateStr",
        }, {
            title: '反馈',
            dataIndex: "remark",
        }
        // ,{
        //     title: '操作',
        //     dataIndex: "",
        //     render: (value,record) => {
        //         return(
        //            <div style={{ textAlign: "left" }}>
        //                 {record.scenes == '30' && record.state == '10' ? <a href="#" onClick={me.checkPayModal.bind(me,record, '查询支付结果', false)}>查询支付结果</a> : '-'}
        //            </div>
        //         )
        //     }
        //  }
        ];

        var state = this.state;
        return (
            <div className="block-panel">
                <Table columns={columns} rowKey={this.rowKey} ref="table" 
                       onRowClick={this.onRowClick}
                       dataSource={this.state.data}
                       rowClassName={this.rowClassName}
                       pagination={this.state.pagination}
                       onChange={this.handleTableChange}
                />
                <Lookdetails ref="Lookdetails" visible={state.visible} title={state.title} hideModal={me.hideModal} record={state.record}
                canEdit={state.canEdit} />
                <AddWin ref="AddWin"  visible={state.visibleAdd} hideModal={me.hideModal} title={state.title}/>
            </div>
        );
    }
})
