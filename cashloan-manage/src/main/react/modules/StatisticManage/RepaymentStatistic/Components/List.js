import React from 'react'
import {Modal, Table} from 'antd';

var confirm = Modal.confirm;
const objectAssign = require('object-assign');
export default React.createClass({
  getInitialState() {
    return {
      selectedRowKeys: [], // 这里配置默认勾选列
      loading: false,
      data: [],
      pagination: {},
      canEdit: true,
      visible: false,
      visibleAdd:false,
    };
  },
  componentWillReceiveProps(nextProps, nextState) {
    this.clearSelectedList();
    this.fetch(nextProps.params);
  },
  hideModal() {
    this.setState({
      visible: false,
      visibleAdd:false
    });
    this.refreshList();
  },
  //新增跟编辑弹窗
  showModal(title, record, canEdit) {
    var record = record;
    this.setState({
      canEdit: canEdit,
      visible: true,
      title: title,
      record: record
    },()=>{
      this.refs.CustomerWin.setFieldsValue(record);
    });
  },
  //新增
  addModal(title, record, canEdit){
      this.setState({
        visibleAdd:true,
        title:title,  
      })

  },
  rowKey(record) {
    return record.id;
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
      url: '/modules/manage/statistic/listRepaymentStatistic.htm',
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
  clearSelectedList() {
    this.setState({
      selectedRowKeys: [],
    });
  },
  refreshList() {
    var pagination = this.state.pagination;
    var params = objectAssign({}, this.props.params, {
      current: pagination.current,
      pageSize: pagination.pageSize,
    });
    this.fetch(params);
  },
  componentDidMount() {
    this.fetch();
  },

  onRowClick(record) {
    this.setState({
      selectedRowKeys: [record.id],
      selectedrecord: record
    });
  },
 
  render() {
    var columns = [{
      title: '统计时间',
      dataIndex: 'countTimeStr',
    }, {
          title: '应还订单笔数',
          dataIndex: 'shouldOrder',
    },{
      title: '待还订单笔数',
      dataIndex: 'remainOrder'
    }, {
      title: '提前还款笔数',
      dataIndex: "advanceRepayment",
    }, {
        title: '正常还款笔数',
        dataIndex: "normalRepayment",
    }, {
        title: '展期还款笔数',
        dataIndex: "extendRepayment",
    }, {
        title: '逾期还款笔数',
        dataIndex: "overdueRepayment",
    }, {
        title: '到期应还金额',
        dataIndex: "shouldAmount",
    }, {
        title: '到期实还金额',
        dataIndex: "realReturnAmount",
    }, {
        title: '到期放款本金',
        dataIndex: "loadAmount",
    },{
        title: '到期待还金额',
        dataIndex: "remainAmount",
    }, {
        title: '今日减免金额',
        dataIndex: "derateAmount",
    }, {
        title: '今日实还金额',
        dataIndex: "todayRealReturnAmount",
    }];
    var state = this.state;
    return (
      <div className="block-panel">
          {/*<div className="actionBtns" style={{ marginBottom: 16 }}>*/}
              {/*<span> 消费总金额：{this.state.totalFee}</span>*/}
              {/*<dev></dev>*/}
          {/*</div>*/}

           <Table columns={columns} rowKey={this.rowKey}
             onRowClick={this.onRowClick}
             dataSource={this.state.data}
             pagination={this.state.pagination}
             loading={this.state.loading}
             onChange={this.handleTableChange}  />
         </div>
    );
  }
})