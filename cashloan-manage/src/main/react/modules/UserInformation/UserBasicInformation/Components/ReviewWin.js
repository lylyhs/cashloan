import React from 'react';
import {
  Modal,
  Form,
  Input,
  Row,
  Col,
  Tabs,
  Button
} from 'antd';
import Tab1 from './Tab1';
import Tab2 from './Tab2';
import Tab3 from './Tab3';
import Tab4 from './Tab4';
import Tab5 from './Tab5';
import Tab6 from './Tab6';
import Tab7 from './Tab7';
const createForm = Form.create;
const FormItem = Form.Item;
const TabPane = Tabs.TabPane;
const objectAssign = require('object-assign');
var ReviewWin = React.createClass({
  getInitialState() {
    return {
      activekey: "1",
      tabClick:false,
    };
  },
  handleCancel() {
    this.changeTabState();
    this.refs.Tab1.resetFields();
    this.props.hideModal();
    
  },

  changeTabState() {
      this.setState({
          activekey: '1',
      })
  },
  handleTabClick(key) {
      this.setState({
          activekey: key,
          tabClick:true,
      })
  },
  tabClickChange(){
      this.setState({
          tabClick:false,
      })
  },
  render() {
    var props = this.props;
    var modalBtns  = [
            <button key="back" className="ant-btn" onClick={this.handleCancel}>关 闭</button>
            ];
    return (
      <Modal key={props.key} title={props.title} visible={props.visible} onCancel={this.handleCancel} width="1200" footer={modalBtns} >
                <Tabs onTabClick={this.handleTabClick}  activekey={this.state.activekey}>
                    <TabPane tab="基本信息" key="1">
                        <Tab1 record={props.record} ref="Tab1" canEdit={props.canEdit} call={this.props.call} recordSoure={props.recordSoure} />
                    </TabPane>
                    <TabPane tab="通讯录" key="2">
                        <Tab2 record={props.record} ref="Tab2" canEdit={props.canEdit} activeKey1={this.state.activekey} tabClick={this.state.tabClick} tabClickChange={this.tabClickChange} />
                    </TabPane>
                    <TabPane tab="通话记录" key="3">
                        <Tab3 record={props.record} ref="Tab3" canEdit={props.canEdit} activeKey1={this.state.activekey}/>
                    </TabPane>
                    {/* <TabPane tab="运营商通话记录" key="4">
                        <Tab4 record={props.record} ref="Tab4" canEdit={props.canEdit} activeKey={this.state.activekey}/>
                    </TabPane>*/}
                    {/* <TabPane tab="短信数据" key="5">
                        <Tab5 record={props.record} ref="Tab5" canEdit={props.canEdit} activeKey1={this.state.activekey}/>
                    </TabPane>*/}
                    <TabPane tab="借款记录" key="6">
                        <Tab6 record={props.record} ref="Tab6" canEdit={props.canEdit} activeKey1={this.state.activekey}/>
                    </TabPane>
                    <TabPane tab="邀请记录" key="7">
                        <Tab7 record={props.record} ref="Tab7" canEdit={props.canEdit} activeKey1={this.state.activekey}/>
                    </TabPane>
                </Tabs>
            </Modal>
    );
  }
});
export default ReviewWin;